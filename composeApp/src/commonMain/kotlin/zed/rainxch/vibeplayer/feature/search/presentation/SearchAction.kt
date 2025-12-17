package zed.rainxch.vibeplayer.feature.search.presentation

import zed.rainxch.vibeplayer.core.domain.model.Music

sealed interface SearchAction {
    data object OnSearchQueryClearClick : SearchAction
    data class OnSearchQueryChange(val query: String) : SearchAction
    data object OnCancelClick : SearchAction
    data class OnMusicClick(val music: Music) : SearchAction
}