package zed.rainxch.vibeplayer.core

interface MediaPlayerController {

    fun play(url: String)
    fun pause()
    fun resume()
    fun stop()

}