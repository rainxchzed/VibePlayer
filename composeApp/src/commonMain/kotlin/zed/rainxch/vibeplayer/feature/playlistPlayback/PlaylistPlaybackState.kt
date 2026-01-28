package zed.rainxch.vibeplayer.feature.playlistPlayback

import org.jetbrains.compose.resources.DrawableResource
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_playlist
import zed.rainxch.vibeplayer.core.domain.model.Music

data class PlaylistPlaybackState(
    val defaultImage: DrawableResource = Res.drawable.ic_playlist,
    val image: String? = null,
    val title: String = "",
    val songs: List<Music> = emptyList()
)
