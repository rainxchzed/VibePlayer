package zed.rainxch.vibeplayer.feature.now_playing.presentation

interface MusicPlaybackAction {
    data object onPlayClick : MusicPlaybackAction
    data object onPauseClick : MusicPlaybackAction
    data object onNextClick : MusicPlaybackAction
    data object onPreviousClick : MusicPlaybackAction
    data class OnSeek(val positionMs: Long) : MusicPlaybackAction
}