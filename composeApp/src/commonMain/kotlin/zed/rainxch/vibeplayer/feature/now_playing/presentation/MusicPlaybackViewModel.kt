package zed.rainxch.vibeplayer.feature.now_playing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import zed.rainxch.vibeplayer.core.domain.MediaPlayerController
import zed.rainxch.vibeplayer.core.domain.model.Music

class MusicPlaybackViewModel(private val playerController: MediaPlayerController) : ViewModel() {

    private val _state = MutableStateFlow(MusicPlaybackState())
    val state = _state.asStateFlow()

    private val _playlist = MutableStateFlow<List<Music>>(emptyList())

    private var progressJob: Job? = null

init {
    playerController.setOnCompletionListener {
        handleTrackCompletion()
    }
}
    fun createPlayList(musicsList: List<Music>) {
        _playlist.value = musicsList
    }

    fun loadSelectedMusic(selectedMusic: Music?) {
        _state.update {
            it.copy(selectedMusic = selectedMusic)
        }
        if (selectedMusic?.musicUrl != null) {
            playMusic(selectedMusic.musicUrl)

            _state.update {
                it.copy(isPlaying = true)
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

    fun stopMusic() {
        playerController.stop()
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
        val currentIndex = _playlist.value.indexOfFirst { it.id == _state.value.selectedMusic?.id }
        val nextIndex = (currentIndex + 1) % _playlist.value.size
        val nextMusic = _playlist.value[nextIndex]
        loadSelectedMusic(nextMusic)
    }

    fun skipToPrevious() {
        val currentIndex = _playlist.value.indexOfFirst { it.id == _state.value.selectedMusic?.id }
        val previousIndex = if (currentIndex <= 0) _playlist.value.lastIndex else currentIndex - 1
        val prevMusic = _playlist.value[previousIndex]
        loadSelectedMusic(prevMusic)
    }

    fun seekTo(positionMs: Long) {
        playerController.seekTo(positionMs)
    }

    fun handleTrackCompletion() {

        val currentMusic = _state.value.selectedMusic ?: return
        val currentIndex = _playlist.value.indexOfFirst { it.id == currentMusic.id }

        when (_state.value.repeatMode) {
            RepeatMode.REPEAT_ONE -> {
                loadSelectedMusic(currentMusic)
            }

            RepeatMode.REPEAT_ALL -> {
                skipToNext()
            }

            RepeatMode.NONE -> {
                // Off: Play next if available, otherwise STOP
                if (currentIndex < _playlist.value.lastIndex) {
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

    fun onAction(musicPlaybackAction: MusicPlaybackAction) {
        when (musicPlaybackAction) {
            MusicPlaybackAction.OnPlayClick -> {

                if (_state.value.selectedMusic != null){
                    resumeMusic()
                } else {
                    _state.value.selectedMusic?.let {
                        playMusic(it.musicUrl)
                    }
                }

                _state.update {
                    it.copy(isPlaying = true)
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
                seekTo(musicPlaybackAction.positionMs)
                _state.update {
                    it.copy(currentProgress = musicPlaybackAction.positionMs)
                }

            }

            MusicPlaybackAction.OnRepeatClick -> {
                val nextMode = when (_state.value.repeatMode) {
                    RepeatMode.NONE -> RepeatMode.REPEAT_ALL
                    RepeatMode.REPEAT_ALL -> RepeatMode.REPEAT_ONE
                    RepeatMode.REPEAT_ONE -> RepeatMode.NONE
                }
                _state.update {
                    it.copy(repeatMode = nextMode)
                }
            }
        }
    }


}