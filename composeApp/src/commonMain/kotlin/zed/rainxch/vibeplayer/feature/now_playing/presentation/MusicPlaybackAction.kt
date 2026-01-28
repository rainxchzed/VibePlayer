package zed.rainxch.vibeplayer.feature.now_playing.presentation

import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.domain.model.Playlist

sealed interface MusicPlaybackAction {
    data object OnMinimizeClick : MusicPlaybackAction
    data object OnPlayClick : MusicPlaybackAction
    data object OnPauseClick : MusicPlaybackAction
    data object OnNextClick : MusicPlaybackAction
    data object OnPreviousClick : MusicPlaybackAction
    data class OnSeek(val positionMs: Long) : MusicPlaybackAction
    data object OnRepeatClick : MusicPlaybackAction
    data object OnShuffleClick : MusicPlaybackAction
    data object OnPlayAllClick : MusicPlaybackAction
    data object OnShuffleAndPlayClick : MusicPlaybackAction

    data class OnToggleFavouriteMusic(
        val music: Music,
        val isFromPlaylistBottomSheet: Boolean = false,
    ) : MusicPlaybackAction

    data object OnAddToPlaylistClick : MusicPlaybackAction
    data object OnCloseAddToPlaylistDialog : MusicPlaybackAction
    data class OnChangeNewPlaylistName(val name: String) : MusicPlaybackAction
    data object OnCloseCreatePlaylistDialog : MusicPlaybackAction
    data object OnCreateNewPlaylistClick : MusicPlaybackAction
    data object OnCreatePlaylistClick : MusicPlaybackAction
    data class OnPlaylistSelected(val playlist: Playlist) : MusicPlaybackAction
}
