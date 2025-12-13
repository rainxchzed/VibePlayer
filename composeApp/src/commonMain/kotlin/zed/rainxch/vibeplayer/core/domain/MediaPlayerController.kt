package zed.rainxch.vibeplayer.core.domain

interface MediaPlayerController {

    fun play(url: String)
    fun pause()
    fun resume()
    fun stop()
    fun getCurrentPosition(): Long
    fun getDuration(): Long

}