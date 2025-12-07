package zed.rainxch.vibeplayer.feature.main.data.data_sources

import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.os.Build
import android.provider.MediaStore
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.feature.main.data.data_source.MusicsDataStore
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class AndroidMusicsDataStore(
    private val context: Context
) : MusicsDataStore {

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

    private suspend fun scanForAudioFiles(): ImmutableList<Music> =
        withContext(Dispatchers.IO) {
            val musics = mutableListOf<Music>()

            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE
            )

            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

            val query: Cursor? = context.contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder
            )

            query?.use { cursor ->
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

                while (cursor.moveToNext()) {
                    val filePath = cursor.getString(dataColumn)
                    val fileSize = cursor.getLong(sizeColumn)
                    val duration = cursor.getLong(durationColumn)

                    // Skip files smaller than 100KB or shorter than 30s
                    if (fileSize > 100_000 && duration > 30_000) {
                        val file = File(filePath)
                        if (file.exists()) {
                            val metadata = getMetadata(filePath)
                            if (metadata != null) {
                                musics.add(metadata)
                            }
                        }
                    }
                }
            }

            musics.toImmutableList()
        }

    private fun getMetadata(filePath: String): Music? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)

            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?: File(filePath).nameWithoutExtension

            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                ?: "Unknown Artist"

            val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLongOrNull() ?: 0L

            val duration = formatDuration((durationMs / 1000).toInt())

            val bannerUrl = extractAlbumArt(retriever, filePath)

            Music(
                title = title,
                duration = duration,
                artist = artist,
                bannerUrl = bannerUrl,
                musicUrl = filePath
            )
        } catch (e: Exception) {
            println("Error reading metadata for $filePath: ${e.message}")
            null
        } finally {
            retriever.release()
        }
    }

    private fun extractAlbumArt(retriever: MediaMetadataRetriever, filePath: String): String? {
        return try {
            val artBytes = retriever.embeddedPicture ?: return null

            val cacheDir = File(context.cacheDir, "album_art")
            cacheDir.mkdirs()

            val fileName = "${File(filePath).nameWithoutExtension}_art.jpg"
            val artFile = File(cacheDir, fileName)

            FileOutputStream(artFile).use { out ->
                out.write(artBytes)
            }

            artFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format(
            Locale.getDefault(),
            "%d:%02d",
            minutes, secs
        )
    }
}