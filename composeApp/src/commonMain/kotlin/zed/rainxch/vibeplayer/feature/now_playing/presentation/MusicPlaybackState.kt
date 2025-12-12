package zed.rainxch.vibeplayer.feature.now_playing.presentation

import zed.rainxch.vibeplayer.core.domain.model.Music

data class MusicPlaybackState(val selectedMusic: Music? = null, val isPlaying: Boolean = false){
}