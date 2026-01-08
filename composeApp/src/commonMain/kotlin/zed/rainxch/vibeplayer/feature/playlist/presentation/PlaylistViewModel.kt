package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository

class PlaylistViewModel(
    private val repository: PlaylistsRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(PlaylistState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadPlaylists()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PlaylistState()
        )

    private fun loadPlaylists() {
        viewModelScope.launch {
            repository.getPlaylistsInfo().collect { playlists ->
                _state.value = _state.value.copy(
                    totalCount = playlists.size + 1,
                    userPlaylists = playlists.map { it.toUi() }
                )
            }
        }
    }

    fun onAction(action: PlaylistAction) {
        when (action) {
            PlaylistAction.OnCreatePlaylistClick -> {
                viewModelScope.launch {
                    repository.createTestPlaylist()
                }
            }
        }
    }

}