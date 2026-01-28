package zed.rainxch.vibeplayer.feature.now_playing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import zed.rainxch.vibeplayer.core.domain.MediaPlayerController
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.feature.now_playing.domain.repository.NowPlayingRepository
import zed.rainxch.vibeplayer.feature.now_playing.presentation.NowPlayingEvent.*

class MusicPlaybackViewModel(
    private val playerController: MediaPlayerController,
    private val nowPlayingRepository: NowPlayingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MusicPlaybackState())
    val state = _state.asStateFlow()

    private val _events = Channel<NowPlayingEvent>()
    val events = _events.receiveAsFlow()

    private val _playlist = MutableStateFlow<List<Music>>(emptyList())
    private val _shuffledPlaylist = MutableStateFlow<List<Music>>(emptyList())
    private var progressJob: Job? = null

    init {
        playerController.setOnCompletionListener {
            handleTrackCompletion()
        }

        observePlaylist()
    }

    private fun observePlaylist() {
        viewModelScope.launch {
            launch {
                nowPlayingRepository.getPlaylists().collect { playlists ->
                    _state.update {
                        it.copy(
                            playlists = playlists.toImmutableList()
                        )
                    }
                }
            }
        }
    }

    fun createPlayList(musicsList: List<Music>) {
        _playlist.value = musicsList
        if (_state.value.shuffleMode == ShuffleMode.ACTIVE) {
            _shuffledPlaylist.value = musicsList.shuffled()
        }
    }

    fun loadSelectedMusic(selectedMusic: Music?) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    selectedMusic = selectedMusic,
                )
            }

            launch(Dispatchers.IO) {
                selectedMusic?.id?.let { id ->
                    _state.update {
                        it.copy(
                            isFavourite = nowPlayingRepository.isMusicFavourite(id)
                        )
                    }
                }
            }

            selectedMusic?.musicUrl?.let {
                playMusic(selectedMusic.musicUrl)

                _state.update {
                    it.copy(isPlaying = true)
                }
            }
        }
    }

    fun playMusic(url: String) {
        playerController.play(url)
        startProgressTracking()
    }

    fun resumeMusic() {
        playerController.resume()
        startProgressTracking()
    }

    fun pauseMusic() {
        playerController.pause()
    }

    fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {

            while (isActive) {
                val currentPosition = playerController.getCurrentPosition()
                val duration = playerController.getDuration()

                _state.update {
                    it.copy(
                        currentProgress = currentPosition,
                        duration = duration
                    )
                }
                delay(500L) // Smoother updates than 1000L
            }
        }
    }

    fun stopProgressTracking() {
        progressJob?.cancel()
    }

    fun skipToNext() {

        val currentPlaylist = if (_state.value.shuffleMode == ShuffleMode.ACTIVE) {
            _shuffledPlaylist.value
        } else {
            _playlist.value
        }

        if (currentPlaylist.isEmpty()) return

        val currentIndex = currentPlaylist.indexOfFirst { it.id == _state.value.selectedMusic?.id }
        val nextIndex = (currentIndex + 1) % currentPlaylist.size
        val nextMusic = currentPlaylist[nextIndex]
        loadSelectedMusic(nextMusic)
    }

    fun skipToPrevious() {

        val currentPlaylist = if (_state.value.shuffleMode == ShuffleMode.ACTIVE) {
            _shuffledPlaylist.value
        } else {
            _playlist.value
        }

        if (currentPlaylist.isEmpty()) return

        val currentIndex = currentPlaylist.indexOfFirst { it.id == _state.value.selectedMusic?.id }
        val previousIndex = if (currentIndex <= 0) currentPlaylist.lastIndex else currentIndex - 1
        val prevMusic = currentPlaylist[previousIndex]
        loadSelectedMusic(prevMusic)
    }

    fun seekTo(positionMs: Long) {
        playerController.seekTo(positionMs)
    }

    fun handleTrackCompletion() {

        val currentList =
            if (_state.value.shuffleMode == ShuffleMode.ACTIVE) _shuffledPlaylist.value else _playlist.value

        val currentMusic = _state.value.selectedMusic ?: return
        val currentIndex = currentList.indexOfFirst { it.id == currentMusic.id }

        when (_state.value.repeatMode) {
            RepeatMode.REPEAT_ONE -> {
                loadSelectedMusic(currentMusic)
            }

            RepeatMode.REPEAT_ALL -> {
                skipToNext()
            }

            RepeatMode.NONE -> {
                // Off: Play next if available, otherwise STOP
                if (currentIndex < currentList.lastIndex) {
                    skipToNext()
                } else {
                    // Last track reached: Stop and reset progress
                    pauseMusic()
                    _state.update {
                        it.copy(isPlaying = false, currentProgress = 0L)
                    }
                }
            }
        }
    }


    fun onAction(action: MusicPlaybackAction) {
        when (action) {
            MusicPlaybackAction.OnPlayClick -> {

                if (_state.value.selectedMusic != null) {
                    resumeMusic()
                    _state.update {
                        it.copy(isPlaying = true)
                    }
                } else {
                    val currentPlaylist = if (_state.value.shuffleMode == ShuffleMode.ACTIVE)
                        _shuffledPlaylist.value else _playlist.value
                    currentPlaylist.firstOrNull()?.let { loadSelectedMusic(it) }
                }


            }

            MusicPlaybackAction.OnPauseClick -> {
                pauseMusic()
                _state.update {
                    it.copy(isPlaying = false)
                }
                stopProgressTracking()
            }

            MusicPlaybackAction.OnNextClick -> {
                skipToNext()
            }

            MusicPlaybackAction.OnPreviousClick -> {
                skipToPrevious()
            }

            is MusicPlaybackAction.OnSeek -> {
                seekTo(action.positionMs)
                _state.update {
                    it.copy(currentProgress = action.positionMs)
                }

            }

            MusicPlaybackAction.OnRepeatClick -> {
                val nextRepeatMode = when (_state.value.repeatMode) {
                    RepeatMode.NONE -> RepeatMode.REPEAT_ALL
                    RepeatMode.REPEAT_ALL -> RepeatMode.REPEAT_ONE
                    RepeatMode.REPEAT_ONE -> RepeatMode.NONE
                }
                _state.update {
                    it.copy(repeatMode = nextRepeatMode)
                }
            }

            MusicPlaybackAction.OnShuffleClick -> {
                val nextShuffleMode = when (_state.value.shuffleMode) {
                    ShuffleMode.INACTIVE -> {
                        _shuffledPlaylist.value = _playlist.value.shuffled()
                        ShuffleMode.ACTIVE
                    }

                    ShuffleMode.ACTIVE -> ShuffleMode.INACTIVE
                }
                _state.update {
                    it.copy(shuffleMode = nextShuffleMode)
                }

            }

            MusicPlaybackAction.OnPlayAllClick -> {
                // Ensure shuffle is disabled
                _state.update {
                    it.copy(shuffleMode = ShuffleMode.INACTIVE)
                }

                // Start playing first track from normal playlist
                _playlist.value.firstOrNull()?.let { firstTrack ->
                    loadSelectedMusic(firstTrack)
                }
            }

            MusicPlaybackAction.OnShuffleAndPlayClick -> {
                // Enable shuffle and regenerate shuffled playlist
                _shuffledPlaylist.value = _playlist.value.shuffled()
                _state.update {
                    it.copy(shuffleMode = ShuffleMode.ACTIVE)
                }

                // Start playing first track from shuffled playlist
                _shuffledPlaylist.value.firstOrNull()?.let { firstTrack ->
                    loadSelectedMusic(firstTrack)
                }
            }

            MusicPlaybackAction.OnMinimizeClick -> {
                // Handled in composable
            }

            MusicPlaybackAction.OnAddToPlaylistClick -> {
                _state.update {
                    it.copy(
                        isSelectPlaylistBottomSheetVisible = true
                    )
                }
            }

            MusicPlaybackAction.OnCloseAddToPlaylistDialog -> {
                _state.update {
                    it.copy(
                        isSelectPlaylistBottomSheetVisible = false
                    )
                }
            }

            MusicPlaybackAction.OnCreatePlaylistClick -> {
                _state.update {
                    it.copy(
                        isSelectPlaylistBottomSheetVisible = false,
                        isCreateNewPlaylistBottomSheetVisible = true
                    )
                }
            }

            is MusicPlaybackAction.OnPlaylistSelected -> {
                viewModelScope.launch {
                    _state.value.selectedMusic?.let { music ->
                        val isInPlaylist = action.playlist.musics.contains(music.id)

                        _state.update {
                            it.copy(
                                isSelectPlaylistBottomSheetVisible = false,
                                isCreateNewPlaylistBottomSheetVisible = false
                            )
                        }

                        if (isInPlaylist) {
                            nowPlayingRepository.removeSongFromPlaylist(
                                musicId = music.id,
                                playlistId = action.playlist.id
                            )
                        } else {
                            nowPlayingRepository.addSongToPlaylist(
                                musicId = music.id,
                                playlistId = action.playlist.id
                            )
                        }

                        _events.send(
                            OnMessage(
                                message = if (isInPlaylist) {
                                    "Removed from the ${action.playlist.title} playlist"
                                } else {
                                    "Added to the playlist ${action.playlist.title}"
                                }
                            )
                        )
                    }

                }
            }

            is MusicPlaybackAction.OnToggleFavouriteMusic -> {
                viewModelScope.launch {
                    val wasAlreadyFavourite = _state.value.isFavourite
                    if (wasAlreadyFavourite) {
                        nowPlayingRepository.removeFavouriteSong(action.music.id)
                    } else {
                        nowPlayingRepository.addFavouriteSong(action.music.id)
                    }

                    _state.update {
                        it.copy(
                            isFavourite = !it.isFavourite,
                        )
                    }

                    if (action.isFromPlaylistBottomSheet) {
                        _state.update {
                            it.copy(
                                isSelectPlaylistBottomSheetVisible = false
                            )
                        }

                        val message = if (wasAlreadyFavourite) {
                            "Removed from Favourites"
                        } else {
                            "Added to Favourites"
                        }
                        _events.send(OnMessage(message))

                    }
                }
            }

            is MusicPlaybackAction.OnChangeNewPlaylistName -> {
                _state.update {
                    it.copy(
                        newPlaylistName = action.name
                    )
                }
            }

            MusicPlaybackAction.OnCloseCreatePlaylistDialog -> {
                _state.update {
                    it.copy(
                        newPlaylistName = "",
                        isCreateNewPlaylistBottomSheetVisible = false
                    )
                }
            }

            MusicPlaybackAction.OnCreateNewPlaylistClick -> {
                viewModelScope.launch {
                    val newPlaylistName = _state.value.newPlaylistName
                    if (newPlaylistName.isBlank()) {
                        _events.send(OnMessage("Playlist name cannot be empty"))
                        return@launch
                    }

                    val newPlaylist = nowPlayingRepository.createNewPlaylist(newPlaylistName)

                    _state.value.selectedMusic?.let { music ->
                        nowPlayingRepository.addSongToPlaylist(
                            musicId = music.id,
                            playlistId = newPlaylist.id
                        )
                    }

                    _events.send(OnMessage("Added to playlist $newPlaylistName"))

                    _state.update {
                        it.copy(
                            newPlaylistName = "",
                            isCreateNewPlaylistBottomSheetVisible = false
                        )
                    }
                }
            }
        }
    }
}
