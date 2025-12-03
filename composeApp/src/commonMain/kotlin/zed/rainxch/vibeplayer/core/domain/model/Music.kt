package zed.rainxch.vibeplayer.core.domain.model

data class Music(
    val title: String,
    val duration: String,
    val artist: String,
    val bannerUrl: String? = null,
    val musicUrl: String
)
