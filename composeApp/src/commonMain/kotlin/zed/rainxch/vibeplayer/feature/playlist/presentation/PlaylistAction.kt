package zed.rainxch.vibeplayer.feature.playlist.presentation

sealed interface PlaylistAction {
    data object OnCreatePlaylistClick : PlaylistAction
}