package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation

data class AddSongsState(
    val selectedMusicIds: Set<Int> = emptySet(),
    val isAddingSongs: Boolean = false
)
