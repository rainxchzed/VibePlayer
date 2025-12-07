package zed.rainxch.vibeplayer.feature.main.data.data_store

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.feature.main.data.data_source.MusicsDataStore
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.UnsupportedAudioFileException

// jvmMain implementation
class JvmMusicsDataStore : MusicsDataStore {

    override fun scanMusics(): ImmutableList<Music> {
        return runBlocking {
            withContext(Dispatchers.IO) {
                scanForAudioFiles()
            }
        }
    }

    override fun checkIfMusicExist(music: Music): Boolean {
        val file = File(music.musicUrl)
        return file.exists() && file.canRead() && file.length() > 0
    }

    private suspend fun scanForAudioFiles(
        directories: List<String> = getDefaultMusicPaths(),
        onProgress: (Int, String) -> Unit = { _, _ -> }
    ): ImmutableList<Music> = withContext(Dispatchers.IO) {
        val audioExtensions = setOf("mp3", "wav", "flac", "m4a", "ogg", "aac", "wma", "opus")
        val musics = mutableListOf<Music>()
        var count = 0

        directories.forEach { path ->
            val directory = File(path)
            if (directory.exists() && directory.isDirectory) {
                try {
                    directory.walkTopDown()
                        .onEnter { dir ->
                            !dir.name.startsWith(".")
                        }
                        .filter { file ->
                            file.isFile &&
                                    file.extension.lowercase() in audioExtensions &&
                                    !file.name.startsWith(".") &&
                                    file.length() > 100_000 // > 100KB
                        }
                        .forEach { file ->
                            val metadata = getMetadata(file.absolutePath)
                            if (metadata != null) {
                                musics.add(metadata)
                                count++
                                onProgress(count, file.name)
                            }
                        }
                } catch (e: Exception) {
                    println("Error scanning directory $path: ${e.message}")
                }
            }
        }

        musics.toImmutableList()
    }

    private fun getDefaultMusicPaths(): List<String> {
        val userHome = System.getProperty("user.home")
        val os = System.getProperty("os.name").lowercase()

        return when {
            "win" in os -> listOf(
                "$userHome\\Music",
                "$userHome\\Downloads"
            )
            "mac" in os -> listOf(
                "$userHome/Music",
                "$userHome/Downloads"
            )
            else -> listOf(
                "$userHome/Music",
                "$userHome/music",
                "$userHome/Downloads"
            )
        }
    }

    private fun getMetadata(filePath: String): Music? {
        return try {
            val file = File(filePath)

            try {
                // Try JAudioTagger first
                val audioFile = AudioFileIO.read(file)
                val tag = audioFile.tag
                val header = audioFile.audioHeader

                Music(
                    title = tag?.getFirst(FieldKey.TITLE)?.takeIf { it.isNotBlank() }
                        ?: file.nameWithoutExtension,
                    duration = formatDuration(header.trackLength),
                    artist = tag?.getFirst(FieldKey.ARTIST)?.takeIf { it.isNotBlank() }
                        ?: "Unknown Artist",
                    bannerUrl = extractAlbumArt(tag),
                    musicUrl = file.absolutePath
                )
            } catch (audioException: Exception) {
                // Fallback: Try Java Sound API for duration
                val duration = try {
                    getDurationWithJavaSound(file)
                } catch (e: Exception) {
                    "0:00"
                }

                Music(
                    title = file.nameWithoutExtension,
                    duration = duration,
                    artist = "Unknown Artist",
                    bannerUrl = null,
                    musicUrl = file.absolutePath
                )
            }
        } catch (e: Exception) {
            println("Error reading metadata for $filePath: ${e.message}")
            null
        }
    }

    private fun getDurationWithJavaSound(file: File): String {
        return try {
            val audioInputStream = AudioSystem.getAudioInputStream(file)
            val format = audioInputStream.format
            val frames = audioInputStream.frameLength
            val durationInSeconds = (frames / format.frameRate).toInt()
            audioInputStream.close()
            formatDuration(durationInSeconds)
        } catch (e: UnsupportedAudioFileException) {
            "0:00"
        }
    }

    private fun extractAlbumArt(tag: org.jaudiotagger.tag.Tag?): String? {
        return try {
            val artwork = tag?.firstArtwork ?: return null
            val tempDir = File(System.getProperty("java.io.tmpdir"), "vibeplayer_art")
            tempDir.mkdirs()

            val artFile = File(tempDir, "${System.currentTimeMillis()}.jpg")
            artFile.writeBytes(artwork.binaryData)
            artFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%d:%02d", minutes, secs)
    }
}