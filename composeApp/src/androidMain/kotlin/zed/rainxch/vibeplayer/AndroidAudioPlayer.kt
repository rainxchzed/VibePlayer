package zed.rainxch.vibeplayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import zed.rainxch.vibeplayer.core.domain.MediaPlayerController

class AndroidAudioPlayer(context: Context) : MediaPlayerController {
    private val exoPlayer = ExoPlayer.Builder(context).build()


    override fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun resume() {
        exoPlayer.play()
    }

    override fun stop() {
        exoPlayer.stop()
    }

    override fun getCurrentPosition(): Long {
        return exoPlayer.currentPosition
    }

    override fun getDuration(): Long {
        val duration = exoPlayer.duration
        return if (duration < 0) 0L else duration
    }
}