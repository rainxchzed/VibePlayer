package zed.rainxch.vibeplayer.feature.playlist.presentation

sealed interface PlaylistAction {
    data object OnCreatePlaylistClick : PlaylistAction
    data object OnDismissBottomSheet : PlaylistAction
    data class OnPlaylistNameChange(val name: String) : PlaylistAction
    data object OnConfirmCreatePlaylist : PlaylistAction
}