package zed.rainxch.vibeplayer.core.data.mappers

import kotlinx.collections.immutable.toImmutableList
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistWithCount
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistWithMusics
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.core.domain.model.PlaylistFull
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo

fun PlaylistWithMusics.toDomain(): PlaylistFull =
    PlaylistFull(
        id = this.playlist.id,
        title = playlist.title,
        musics = musics.map { it.toMusic() }.toImmutableList(),
        coverImage = playlist.coverImage
    )

fun PlaylistWithCount.toDomain(): PlaylistInfo =
    PlaylistInfo(
        id = playlist.id,
        title = playlist.title,
        musicCount = musicCount,
        coverImage = playlist.coverImage
    )
