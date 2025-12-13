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
                _state.update {
                    it.copy(currentProgress = currentPosition)
                }

                val duration = playerController.getDuration()
                _state.update {
                    it.copy(duration = duration)
                }
                delay(1000L)
            }
        }
    }

    fun stopProgressTracking() {
        progressJob?.cancel()
    }


    fun skipToNext() {
        val currentIndex = _playlist.value.indexOfFirst { it.id == _state.value.selectedMusic?.id }
        if (currentIndex < _playlist.value.lastIndex) {
            val nextMusic = _playlist.value[currentIndex + 1]
            loadSelectedMusic(nextMusic)
        }
    }

    fun skipToPrevious() {
        val currentIndex = _playlist.value.indexOfFirst { it.id == _state.value.selectedMusic?.id }
        if (currentIndex > 0) {
            val prevMusic = _playlist.value[currentIndex - 1]
            loadSelectedMusic(prevMusic)
        }
    }

    fun onAction(musicPlaybackAction: MusicPlaybackAction) {
        when (musicPlaybackAction) {
            MusicPlaybackAction.onPlayClick -> {

                _state.value.selectedMusic?.let {
                    playMusic(it.musicUrl)
                }
                _state.update {
                    it.copy(isPlaying = true)
                }
            }

            MusicPlaybackAction.onPauseClick -> {
                pauseMusic()
                _state.update {
                    it.copy(isPlaying = false)
                }
                stopProgressTracking()
            }

            MusicPlaybackAction.onNextClick -> {
                skipToNext()
            }

            MusicPlaybackAction.onPreviousClick -> {
                skipToPrevious()
            }
        }
    }


}