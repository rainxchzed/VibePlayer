package zed.rainxch.vibeplayer.feature.songs.data.data_sources

import kotlinx.cinterop.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import platform.Foundation.*
import platform.AVFoundation.*
import platform.UIKit.UIImage
import kotlinx.coroutines.*
import platform.CoreGraphics.CGSizeMake
import platform.MediaPlayer.*
import platform.UIKit.UIImageJPEGRepresentation
import zed.rainxch.vibeplayer.core.data.data_source.MusicsDataStore
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.presentation.utils.formatMilliseconds

class IOSMusicsDataStore : MusicsDataStore {

    override fun scanMusics(): ImmutableList<Music> {
        return runBlocking {
            withContext(Dispatchers.Default) {
                scanForAudioFiles()
            }
        }
    }

    override fun checkIfMusicExist(music: Music): Boolean {
        val fileManager = NSFileManager.defaultManager
        return fileManager.fileExistsAtPath(music.musicUrl)
    }

    private suspend fun scanForAudioFiles(): ImmutableList<Music> =
        withContext(Dispatchers.Default) {
            val musics = mutableListOf<Music>()

            val query = MPMediaQuery.songsQuery()
            val items = query.items ?: return@withContext emptyList<Music>().toImmutableList()

            items.forEach { item ->
                val mediaItem = item as? MPMediaItem ?: return@forEach
                
                // Get asset URL
                val assetURL = mediaItem.assetURL ?: return@forEach
                val urlString = assetURL.absoluteString ?: return@forEach
                
                // Filter by duration (30+ seconds) and valid URL
                val duration = mediaItem.playbackDuration
                if (duration > 30.0) {
                    val metadata = getMetadata(mediaItem, urlString)
                    metadata?.let { musics.add(it) }
                }
            }

            musics.toImmutableList()
        }

    private fun getMetadata(mediaItem: MPMediaItem, urlString: String): Music? {
        return try {
            val title = mediaItem.title ?: "Unknown Title"
            val artist = mediaItem.artist ?: "Unknown Artist"
            val durationSeconds = mediaItem.playbackDuration.toInt()
            val duration = formatDuration(durationSeconds)
            
            // Extract album art
            val bannerUrl = extractAlbumArt(mediaItem, urlString)

            Music(
                title = title,
                duration = duration,
                artist = artist,
                bannerUrl = bannerUrl,
                musicUrl = urlString,
                isFavourite = false
            )
        } catch (e: Exception) {
            println("Error reading metadata for $urlString: ${e.message}")
            null
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun extractAlbumArt(mediaItem: MPMediaItem, urlString: String): String? {
        return try {
            val artwork = mediaItem.artwork ?: return null
            
            // Get the image from artwork
            val image = artwork.imageWithSize(CGSizeMake(300.0, 300.0)) ?: return null
            
            // Convert to JPEG data
            val imageData = UIImageJPEGRepresentation(image, 0.8) ?: return null
            
            // Save to cache directory
            val cacheDir = NSFileManager.defaultManager.URLsForDirectory(
                NSCachesDirectory,
                NSUserDomainMask
            ).firstOrNull() as? NSURL ?: return null
            
            val albumArtDir = cacheDir.URLByAppendingPathComponent("album_art", true)
            
            // Create directory if needed
            NSFileManager.defaultManager.createDirectoryAtURL(
                albumArtDir!!,
                true,
                null,
                null
            )
            
            // Create unique filename
            val fileName = "${urlString.hashCode()}_art.jpg"
            val fileURL = albumArtDir.URLByAppendingPathComponent(fileName, false)
            
            // Write image data to file
            imageData.writeToURL(fileURL!!, true)
            
            fileURL.path
        } catch (e: Exception) {
            println("Error extracting album art: ${e.message}")
            null
        }
    }

    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return formatMilliseconds(seconds * 1000L)
    }

}