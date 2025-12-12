package zed.rainxch.vibeplayer.feature.now_playing.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import zed.rainxch.vibeplayer.core.MediaPlayerController
import zed.rainxch.vibeplayer.core.domain.model.Music

class MusicPlaybackViewModel(private val playerController: MediaPlayerController) : ViewModel() {

    private val _state = MutableStateFlow(MusicPlaybackState())
    val state = _state.asStateFlow()

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
    }

    fun pauseMusic() {
        playerController.pause()
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
            }

            MusicPlaybackAction.onNextClick -> {

            }

            MusicPlaybackAction.onPreviousClick -> {

            }
        }
    }
}