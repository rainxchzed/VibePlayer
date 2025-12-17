package zed.rainxch.vibeplayer.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.feature.search.domain.repository.SearchRepository
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private var searchJob: Job? = null

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.OnCancelClick -> {
                // Handled in composable
            }

            is SearchAction.OnMusicClick -> {
                // Handled in composable
            }

            is SearchAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = action.query
                    )
                }

                performSearch()
            }

            SearchAction.OnSearchQueryClearClick -> {
                _state.update {
                    it.copy(
                        searchQuery = "",
                    )
                }

                performSearch()
            }
        }
    }

    private fun performSearch() {
        val query = _state.value.searchQuery

        if (query.isBlank()) {
            _state.update {
                it.copy(
                    isClearQueryVisible = false,
                    isLoading = false,
//                    musics = persistentListOf()
                )
            }

            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay((200..500).random().milliseconds)

            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val musics = searchRepository.performSearch(query)

            _state.update {
                it.copy(
                    musics = musics,
                    isLoading = false
                )
            }
        }
    }

}