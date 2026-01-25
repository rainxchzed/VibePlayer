package zed.rainxch.vibeplayer.feature.playlistPlayback

import zed.rainxch.vibeplayer.core.domain.model.Music

data class PlaylistPlaybackState(
    val image: String? = null,
    val title: String = "",
    val songs: List<Music> = emptyList()
)