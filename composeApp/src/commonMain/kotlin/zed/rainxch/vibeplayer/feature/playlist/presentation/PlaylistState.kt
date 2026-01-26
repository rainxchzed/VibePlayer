package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.compose.runtime.Stable
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo

@Stable
data class PlaylistState(
    val userPlaylistTotalCount: Int = 0,
    val totalPlaylistCount: Int = 0,
    val favouritesCount: Int = 0,
    val userPlaylists: List<PlaylistCardUi> = emptyList(),
    val systemPlaylists: List<PlaylistCardUi> = emptyList(),
    val showBottomSheet: SheetContent? = null,
    val newPlaylistName: String = ""
)
//    val showBottomSheet: Long? = null,
data class PlaylistCardUi(
    val id: Int,
    val title: String,
    val songsCount: Int,
    val coverImage: String? = null,
)

/*fun Playlist.toUi(): PlaylistCardUi =
    PlaylistCardUi(
        title = title,
        songsCount = musics.size,
        coverImage = coverImage
    )*/

fun PlaylistInfo.toUi(): PlaylistCardUi =
    PlaylistCardUi(
        id = id,
        title = title,
        songsCount = musicCount,
        coverImage = coverImage
    )