package zed.rainxch.vibeplayer.feature.main.data.repository

import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.os.Build
import android.provider.MediaStore
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.feature.main.domain.repository.MainRepository
import java.io.File
import java.io.FileOutputStream

class DefaultMainRepository(
    private val context: Context
) : MainRepository {
    override suspend fun getMusicsWithMetadata(): ImmutableList<Music> =
        withContext(Dispatchers.IO) {
            val audioFiles = mutableListOf<File>()

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

                while (cursor.moveToNext()) {
                    val filePath = cursor.getString(dataColumn)
                    val fileSize = cursor.getLong(sizeColumn)

                    // Skip files smaller than 100KB (likely corrupted or ringtones)
                    if (fileSize > 100_000) {
                        val file = File(filePath)
                        if (file.exists()) {
                            audioFiles.add(file)
                        }
                    }
                }
            }

            audioFiles
                .mapNotNull {
                    getMetadata(it.absolutePath)
                }
                .toImmutableList()
        }

    fun getMetadata(filePath: String): Music? {
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

            // Save to app's cache directory
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
        return String.format("%d:%02d", minutes, secs)
    }
}