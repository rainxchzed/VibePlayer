package zed.rainxch.vibeplayer.core.data.mappers

import kotlinx.collections.immutable.toImmutableList
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistWithMusics
import zed.rainxch.vibeplayer.core.domain.model.Playlist

fun PlaylistWithMusics.toDomain(): Playlist =
    Playlist(
        title = playlist.title,
        musics = musics.map { it.id }.toImmutableList(),
        coverImage = playlist.coverImage
    )
