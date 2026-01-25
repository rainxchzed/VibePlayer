package zed.rainxch.vibeplayer.feature.playlistPlayback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository

class PlaylistPlaybackViewModel(
    private val id: Int,
    private val repository: PlaylistsRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(PlaylistPlaybackState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                subscribeOnPlaylistInfo()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PlaylistPlaybackState()
        )

    fun onAction(action: PlaylistPlaybackAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

    private fun subscribeOnPlaylistInfo() {
        viewModelScope.launch {
            repository.getPlaylistWithMusics(id).collectLatest { playlistFull ->
                _state.update {
                    it.copy(
                        title = playlistFull.title,
                        songs = playlistFull.musics,
                        image = playlistFull.coverImage
                    )
                }
            }
        }
    }

}