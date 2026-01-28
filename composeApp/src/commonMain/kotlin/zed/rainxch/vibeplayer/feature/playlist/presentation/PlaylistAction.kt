package zed.rainxch.vibeplayer.feature.playlist.presentation

sealed interface PlaylistAction {
    data object OnCreatePlaylistClick : PlaylistAction
    data object OnDismissBottomSheet : PlaylistAction
    data class OnPlaylistNameChange(val name: String) : PlaylistAction
    data object OnConfirmCreatePlaylist : PlaylistAction


    data class OnPlaylistMoreOptions(
        val id: Int,
        val title: String,
        val songsCount: Int,
        val coverImage: String? = null,
    ) : PlaylistAction

    data class OnSystemPlaylistMoreOptions(
        val id: Int,
        val title: String,
        val songsCount: Int,
    ) : PlaylistAction

    // Actions for the Options Sheet
    data class OnPlayPlaylistClick(val playlistId: Int) : PlaylistAction
    data class OnRenamePlaylistClick(val playlistId: Int, val currentName: String) : PlaylistAction // Triggers rename UI
    data class OnChangeCoverClick(val playlistId: Int) : PlaylistAction
    data class OnDeletePlaylistClick(val playlistId: Int, val playlistName: String) : PlaylistAction // Triggers delete confirmation UI

    // Rename Playlist
    data class OnCurrentPlaylistNameChange(val name: String) : PlaylistAction
    data class OnConfirmRenamePlaylist(val playlistId: Int) : PlaylistAction

    data class OnConfirmDeletePlaylist(val playlistId: Int) : PlaylistAction

    data class OnCoverImageSelected(val playlistId: Int, val imagePath: String?) : PlaylistAction
    data object OnImagePickerDismissed : PlaylistAction


}