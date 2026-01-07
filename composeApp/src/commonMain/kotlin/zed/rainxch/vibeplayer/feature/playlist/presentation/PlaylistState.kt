package zed.rainxch.vibeplayer.feature.playlist.presentation

import org.jetbrains.compose.resources.DrawableResource

data class PlaylistState(
    val totalCount: Int = 1,
    val favouritesCount: Int = 0,
    val userPlaylists: List<PlaylistCardUi> = emptyList()
)

data class PlaylistCardUi(
    val title: String,
    val songsCount: Int,
    val albumArtUrl: String? = null,
)