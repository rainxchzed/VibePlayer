package zed.rainxch.vibeplayer.feature.now_playing.presentation

sealed interface NowPlayingEvent {
    data class OnMessage(val message: String) : NowPlayingEvent
}