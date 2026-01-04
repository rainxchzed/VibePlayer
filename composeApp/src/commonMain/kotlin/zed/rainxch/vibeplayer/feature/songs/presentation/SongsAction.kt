package zed.rainxch.vibeplayer.feature.songs.presentation

import zed.rainxch.vibeplayer.core.domain.model.Music

sealed interface SongsAction {
    data object OnScanAgainClick : SongsAction
    data class OnMusicItemClick(val music: Music) : SongsAction
    data object OnMinimizeNowPlaying : SongsAction
}