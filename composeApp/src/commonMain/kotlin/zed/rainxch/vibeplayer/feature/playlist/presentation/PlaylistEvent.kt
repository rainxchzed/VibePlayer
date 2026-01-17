package zed.rainxch.vibeplayer.feature.playlist.presentation

sealed interface PlaylistEvent {
    data class ShowSnackbar(val message: String) : PlaylistEvent
}
