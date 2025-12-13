package zed.rainxch.vibeplayer.core.data

import javafx.application.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.*
import zed.rainxch.vibeplayer.core.domain.MediaPlayerController
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class JvmAudioPlayer : MediaPlayerController {
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private val isInitialized = AtomicBoolean(false)

        private fun initializeJavaFX() {
            if (isInitialized.compareAndSet(false, true)) {
                try {
                    Platform.startup {}
                } catch (e: Exception) {
                    // Already initialized
                }
            }
        }
    }

    init {
        initializeJavaFX()
    }

    override fun play(url: String) {
        Platform.runLater {
            try {
                // Stop any existing player
                mediaPlayer?.apply {
                    stop()
                    dispose()
                }

                val mediaUrl = if (url.startsWith("http://") || url.startsWith("https://")) {
                    url
                } else {
                    File(url).toURI().toString()
                }

                println("Playing: $mediaUrl")

                val media = Media(mediaUrl)
                mediaPlayer = MediaPlayer(media).apply {
                    setOnReady {
                        println("Media ready, starting playback")
                        play()
                    }
                    setOnError {
                        println("Media error: ${error?.message}")
                        error?.printStackTrace()
                    }
                    setOnPlaying {
                        println("Media is playing")
                    }
                    setOnEndOfMedia {
                        println("Media ended")
                    }
                }
            } catch (e: Exception) {
                println("Exception in play: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    override fun pause() {
        Platform.runLater {
            try {
                mediaPlayer?.pause()
                println("Paused")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun resume() {
        Platform.runLater {
            try {
                mediaPlayer?.play()
                println("Resumed")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun stop() {
        Platform.runLater {
            try {
                mediaPlayer?.apply {
                    stop()
                    dispose()
                }
                mediaPlayer = null
                println("Stopped")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getCurrentPosition(): Long {
        return runBlocking {
            suspendCoroutine { continuation ->
                Platform.runLater {
                    try {
                        val position = mediaPlayer?.currentTime?.toMillis()?.toLong() ?: 0L
                        continuation.resume(position)
                    } catch (e: Exception) {
                        continuation.resume(0L)
                    }
                }
            }
        }
    }

    override fun getDuration(): Long {
        return runBlocking {
            suspendCoroutine { continuation ->
                Platform.runLater {
                    try {
                        val duration = mediaPlayer?.totalDuration?.toMillis() ?: 0.0
                        val result = if (duration < 0) 0L else duration.toLong()
                        continuation.resume(result)
                    } catch (e: Exception) {
                        continuation.resume(0L)
                    }
                }
            }
        }
    }
}