package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private const val MAX_PLAYLIST_NAME_LENGTH = 40

@OptIn(ExperimentalTime::class)
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

    private val _events = Channel<PlaylistEvent>()
    val events = _events.receiveAsFlow()

    private fun loadPlaylists() {
        viewModelScope.launch {
            repository.getPlaylistsInfo().collect { playlists ->
                _state.update {
                    it.copy(
                        totalCount = playlists.size + 1,
                        userPlaylists = playlists.map { it.toUi() }
                    )
                }
            }
        }
    }

    fun onAction(action: PlaylistAction) {
        when (action) {
            PlaylistAction.OnCreatePlaylistClick -> onCreatePlaylistClick()
            PlaylistAction.OnDismissBottomSheet -> onDismissBottomSheet()
            is PlaylistAction.OnPlaylistNameChange -> onPlaylistNameChange(action.name)
            PlaylistAction.OnConfirmCreatePlaylist -> onConfirmCreatePlaylist()
        }
    }

    private fun onCreatePlaylistClick() {
        _state.update {
            it.copy(
                showBottomSheet = Clock.System.now().toEpochMilliseconds(),
                newPlaylistName = ""
            )
        }
    }

    private fun onDismissBottomSheet() {
        _state.update {
            it.copy(
                showBottomSheet = null,
                newPlaylistName = ""
            )
        }
    }

    private fun onPlaylistNameChange(name: String) {
        if (name.length <= MAX_PLAYLIST_NAME_LENGTH) {
            _state.update {
                it.copy(newPlaylistName = name)
            }
        }
    }

    private fun onConfirmCreatePlaylist() {
        viewModelScope.launch {
            val result = repository.createPlaylist(_state.value.newPlaylistName)
            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            showBottomSheet = null,
                            newPlaylistName = ""
                        )
                    }
                },
                onFailure = { error ->
                    _events.send(
                        PlaylistEvent.ShowSnackbar(
                            error.message ?: "Failed to create playlist"
                        )
                    )
                }
            )
        }
    }

}