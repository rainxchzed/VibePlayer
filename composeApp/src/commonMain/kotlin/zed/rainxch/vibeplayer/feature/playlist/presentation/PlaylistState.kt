package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.compose.runtime.Stable
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo

@Stable
data class PlaylistState(
    val totalCount: Int = 1,
    val favouritesCount: Int = 0,
    val userPlaylists: List<PlaylistCardUi> = emptyList(),
    val showBottomSheet: Long? = null,
    val newPlaylistName: String = ""
)

data class PlaylistCardUi(
    val title: String,
    val songsCount: Int,
    val coverImage: String? = null,
)

fun Playlist.toUi(): PlaylistCardUi =
    PlaylistCardUi(
        title = title,
        songsCount = musics.size,
        coverImage = coverImage
    )

fun PlaylistInfo.toUi(): PlaylistCardUi =
    PlaylistCardUi(
        title = title,
        songsCount = musicCount,
        coverImage = coverImage
    )