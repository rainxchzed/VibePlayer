package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository

class AddSongsViewModel(private val playlistsRepository: PlaylistsRepository) : ViewModel() {

    private val _state = MutableStateFlow(AddSongsState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<AddSongsEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onAction(action: AddSongsAction) {
        when (action) {
            is AddSongsAction.OnSongSelected -> {
                val musicId = action.musicId
                val isSelected = action.isSelected

                _state.update { currentState ->
                    val newSelectedIds = currentState.selectedMusicIds.toMutableSet()

                    if (isSelected)
                        newSelectedIds.add(musicId)
                    else
                        newSelectedIds.remove(musicId)

                    currentState.copy(selectedMusicIds = newSelectedIds)
                }

            }

            is AddSongsAction.OnSelectedAllSongs -> {
                val isSelected = action.isSelected
                val musicsList = action.musics

                _state.update { currentState ->
                    val allIds = if (isSelected) {
                        musicsList.map { it.id }.toSet()
                    } else
                        emptySet()

                    currentState.copy(selectedMusicIds = allIds)
                }
            }

            is AddSongsAction.OnConfirm -> {

                val playListId = action.playlistId

                if (_state.value.isAddingSongs) return

                viewModelScope.launch {
                    _state.update { it.copy(isAddingSongs = true) }
                    playlistsRepository.addSongsToPlaylist(
                        playlistId = playListId,
                        songIds = state.value.selectedMusicIds.toList()
                    )
                    delay(1000)

                    _state.update { it.copy(isAddingSongs = false) }

                    _eventChannel.send(AddSongsEvent.NavigateBack)
                }
            }

            is AddSongsAction.OnClearSelection -> {
                _state.update {
                    it.copy(
                        selectedMusicIds = emptySet(),
                        isAddingSongs = false // Also reset loading state
                    )
                }
            }
        }
    }
}