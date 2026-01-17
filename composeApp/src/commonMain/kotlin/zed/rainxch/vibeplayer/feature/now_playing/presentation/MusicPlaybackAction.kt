package zed.rainxch.vibeplayer.feature.now_playing.presentation

interface MusicPlaybackAction {
    data object OnPlayClick : MusicPlaybackAction
    data object OnPauseClick : MusicPlaybackAction
    data object OnNextClick : MusicPlaybackAction
    data object OnPreviousClick : MusicPlaybackAction
    data class OnSeek(val positionMs: Long) : MusicPlaybackAction
    data object OnRepeatClick: MusicPlaybackAction
    data object OnShuffleClick: MusicPlaybackAction
    data object OnPlayAllClick : MusicPlaybackAction
    data object OnShuffleAndPlayClick : MusicPlaybackAction

}
