package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.DrawableResource

@Stable
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