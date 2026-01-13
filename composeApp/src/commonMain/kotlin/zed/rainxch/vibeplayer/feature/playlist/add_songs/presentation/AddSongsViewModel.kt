package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.core.domain.repository.MusicRepository
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository
import zed.rainxch.vibeplayer.feature.search.domain.repository.SearchRepository

class AddSongsViewModel(private val musicRepository: MusicRepository,private val playlistsRepository: PlaylistsRepository, private val searchRepository: SearchRepository) : ViewModel() {

    private val _state = MutableStateFlow(AddSongsState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<AddSongsEvent>()
    val events = _eventChannel.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            musicRepository.getMusicsWithMetadataFlow().collectLatest { musicList ->
                _state.update {
                    val currentQuery = it.searchQuery
                    val newFilteredList = if (currentQuery.isBlank()) {
                        musicList
                    } else {
                        searchRepository.performSearch(currentQuery)
                    }
                    it.copy(
                        allMusic = musicList,
                        filteredMusic = newFilteredList
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)

            if (query.isBlank()) {
                _state.update { it.copy(filteredMusic = it.allMusic) }
                return@launch
            }

            val results = searchRepository.performSearch(query)
            _state.update { it.copy(filteredMusic = results) }
        }
    }

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
            is AddSongsAction.OnSearchQueryChange -> {
                onSearchQueryChanged(action.query)
            }

            is AddSongsAction.OnSearchQueryClearClick -> {
                onSearchQueryChanged("")
            }
        }
    }
}