package zed.rainxch.vibeplayer.core.domain

import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.Foundation.NSURL

class IosAudioPlayer : MediaPlayerController{

 private var avPlayer: AVPlayer? = null


    override fun play(url: String) {
    val url = NSURL.URLWithString(url) ?: return
    val playerItem = AVPlayerItem(uRL = url)

        avPlayer = AVPlayer(playerItem = playerItem)
        avPlayer?.play()
   }

    override fun pause() {
   avPlayer?.pause() }

    override fun resume() {
        avPlayer?.play()
    }

    override fun stop() {
        avPlayer?.pause()
        avPlayer = null // Release the player
    }

    override fun getCurrentPosition(): Long {
        TODO("Not yet implemented")
    }

    override fun getDuration(): Long {
        TODO("Not yet implemented")
    }
}