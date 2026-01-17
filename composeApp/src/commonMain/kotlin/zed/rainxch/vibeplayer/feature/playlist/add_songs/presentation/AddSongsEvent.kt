package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation

sealed interface AddSongsEvent {
    data object NavigateBack : AddSongsEvent
}
