package zed.rainxch.vibeplayer.feature.playlist.presentation.model

data class PlaylistCardUi(
    val id: Int,
    val title: String,
    val songsCount: Int,
    val coverImage: String? = null,
)