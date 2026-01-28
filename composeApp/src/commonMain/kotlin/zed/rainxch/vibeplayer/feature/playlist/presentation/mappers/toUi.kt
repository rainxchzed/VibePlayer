package zed.rainxch.vibeplayer.feature.playlist.presentation.mappers

import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo
import zed.rainxch.vibeplayer.feature.playlist.presentation.model.PlaylistCardUi

fun PlaylistInfo.toUi(): PlaylistCardUi =
    PlaylistCardUi(
        id = id,
        title = title,
        songsCount = musicCount,
        coverImage = coverImage
    )