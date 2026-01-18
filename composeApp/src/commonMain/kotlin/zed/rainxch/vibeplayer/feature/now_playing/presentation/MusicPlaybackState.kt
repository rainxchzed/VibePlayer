package zed.rainxch.vibeplayer.feature.now_playing.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.domain.model.Playlist

data class MusicPlaybackState(
    val selectedMusic: Music? = null,
    val isPlaying: Boolean = false,
    val isFavourite: Boolean = false,
    val currentProgress: Long = 0L,
    val duration: Long = 0L,
    val newPlaylistName: String = "",
    val isSelectPlaylistBottomSheetVisible: Boolean = false,
    val isCreateNewPlaylistBottomSheetVisible: Boolean = false,
    val playlists: ImmutableList<Playlist> = persistentListOf(),
    val favouriteSongsCount: Int = 0,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val shuffleMode: ShuffleMode = ShuffleMode.INACTIVE
)

enum class RepeatMode {
    NONE,
    REPEAT_ALL,
    REPEAT_ONE
}

enum class ShuffleMode {
    INACTIVE,
    ACTIVE
}