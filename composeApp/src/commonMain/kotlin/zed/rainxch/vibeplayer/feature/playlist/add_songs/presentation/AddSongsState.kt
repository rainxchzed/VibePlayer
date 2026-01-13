package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation

import zed.rainxch.vibeplayer.core.domain.model.Music

data class AddSongsState(
    val allMusic: List<Music> = emptyList(),
    val selectedMusicIds: Set<Int> = emptySet(),
    val isAddingSongs: Boolean = false,
    val searchQuery: String = "",
    val filteredMusic: List<Music> = emptyList()
)
