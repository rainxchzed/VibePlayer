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
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository
import zed.rainxch.vibeplayer.feature.playlist.presentation.SheetContent.CreatePlaylist
import zed.rainxch.vibeplayer.feature.playlist.presentation.SheetContent.DeletePlaylist
import zed.rainxch.vibeplayer.feature.playlist.presentation.SheetContent.RenamePlaylist
import zed.rainxch.vibeplayer.feature.playlist.presentation.SheetContent.ShowPlaylistActions
import kotlin.time.ExperimentalTime

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
            repository
                .getPlaylistsInfo()
                .collect { playlists ->
                    _state.update { currentState ->
                        val (system, user) = playlists.partition {
                            it.id == AppDatabase.FAVOURITES_PLAYLIST_ID
                        }

                        currentState.copy(
                            userPlaylistTotalCount = user.size,
                            userPlaylists = user.map { it.toUi() },
                            systemPlaylists = system.map { it.toUi() },
                            totalPlaylistCount = user.size + system.size
                        )
                    }
                }
        }
    }

    fun onAction(action: PlaylistAction) {
        when (action) {
            PlaylistAction.OnCreatePlaylistClick -> {
                _state.update { it.copy(showBottomSheet = CreatePlaylist) }
            }

            is PlaylistAction.OnPlaylistMoreOptions -> {
                _state.update {
                    it.copy(
                        showBottomSheet = ShowPlaylistActions(
                            id = action.id,
                            title = action.title,
                            songsCount = action.songsCount,
                            coverImage = action.coverImage
                        )
                    )
                }
            }

            PlaylistAction.OnDismissBottomSheet -> onDismissBottomSheet()
            is PlaylistAction.OnPlaylistNameChange -> onPlaylistNameChange(action.name)
            PlaylistAction.OnConfirmCreatePlaylist -> onConfirmCreatePlaylist()


            is PlaylistAction.OnCurrentPlaylistNameChange -> {
                onCurrentPlaylistNameChange(name = action.name)
            }

            is PlaylistAction.OnChangeCoverClick -> Unit
            is PlaylistAction.OnConfirmRenamePlaylist -> onConfirmRenamePlaylist(
                playlistId = action.playlistId,
            )

            is PlaylistAction.OnDeletePlaylistClick -> {
                _state.update {
                    it.copy(
                        showBottomSheet = DeletePlaylist(
                            playListId = action.playlistId,
                            playlistName = action.playlistName
                        )
                    )
                }

            }

            is PlaylistAction.OnConfirmDeletePlaylist -> {
                onConfirmDeletePlaylist(playlistId = action.playlistId)
            }

            is PlaylistAction.OnPlayPlaylistClick -> {
                onNavigateToPlaylistPlayback(playlistId = action.playlistId)

            }

            is PlaylistAction.OnRenamePlaylistClick -> {
                _state.update { it.copy(showBottomSheet = RenamePlaylist(playListId = action.playlistId)) }

            }
        }
    }

    fun onNavigateToPlaylistPlayback(playlistId: Int) {

        viewModelScope.launch {
            _events.send(
                PlaylistEvent.OnNavigateToPlaylistPlayback(
                    playlistId = playlistId,
                    startPlaylistPlayback = true
                )
            )
        }
    }

    private fun onCurrentPlaylistNameChange(name: String) {
        if (name.length <= MAX_PLAYLIST_NAME_LENGTH) {
            _state.update {
                it.copy(currentPlaylistName = name)
            }
        }
    }

    fun onConfirmRenamePlaylist(playlistId: Int) {
        viewModelScope.launch {
            val result = repository.renamePlaylist(
                playlistId = playlistId,
                changedName = _state.value.currentPlaylistName
            )
            result.fold(onSuccess = {
                _state.update {
                    it.copy(
                        showBottomSheet = null,
                        currentPlaylistName = ""
                    )
                }


            }, onFailure = { error ->
                _events.send(
                    PlaylistEvent.ShowSnackbar(
                        error.message ?: "Failed to rename playlist"
                    )
                )

            })

        }
    }

    fun onConfirmDeletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            val result = repository.deletePlaylist(playlistId = playlistId)
            result.fold(onSuccess = {
                _state.update {
                    it.copy(
                        showBottomSheet = null
                    )
                }


            }, onFailure = { error ->
                _events.send(
                    PlaylistEvent.ShowSnackbar(
                        error.message ?: "Failed to delete playlist"
                    )
                )

            })

        }
    }


    private fun onDismissBottomSheet() {
        _state.update {
            it.copy(
                showBottomSheet = null,
                newPlaylistName = "",
                currentPlaylistName = ""
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
                onSuccess = { playlistId ->
                    _state.update {
                        it.copy(
                            showBottomSheet = null,
                            newPlaylistName = ""
                        )
                    }

                    _events.send(PlaylistEvent.OnNavigateToAddSongs(playlistId))
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