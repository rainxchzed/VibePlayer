package zed.rainxch.vibeplayer.feature.playlist.presentation

sealed interface PlaylistEvent {
    data class ShowSnackbar(val message: String) : PlaylistEvent
    data class OnNavigateToAddSongs(val playlistId: Int) : PlaylistEvent

    data class OnNavigateToPlaylistPlayback(val playlistId: Int, val startPlaylistPlayback: Boolean) : PlaylistEvent
}
