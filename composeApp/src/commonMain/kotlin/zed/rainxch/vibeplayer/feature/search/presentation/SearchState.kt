package zed.rainxch.vibeplayer.feature.search.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import zed.rainxch.vibeplayer.core.domain.model.Music

data class SearchState(
    val searchQuery: String = "",
    val isClearQueryVisible: Boolean = false,
    val musics: ImmutableList<Music> = persistentListOf(),
    val isLoading: Boolean = false
)