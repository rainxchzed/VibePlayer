package zed.rainxch.vibeplayer.feature.now_playing.presentation

import zed.rainxch.vibeplayer.core.domain.model.Music

data class MusicPlaybackState(
    val selectedMusic: Music? = null,
    val isPlaying: Boolean = false,
    val currentProgress: Long = 0L,
    val duration : Long = 0L,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val shuffleMode: ShuffleMode = ShuffleMode.INACTIVE
)

enum class RepeatMode{
    NONE,
    REPEAT_ALL,
    REPEAT_ONE
}

enum class ShuffleMode {
    INACTIVE,
    ACTIVE
}