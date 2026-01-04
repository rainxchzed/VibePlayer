package zed.rainxch.vibeplayer.feature.main_controller.presentation.model

enum class MainControllerTabs(
    val index: Int,
    val label: String
) {
    Songs(
        index = 0,
        label = "Songs"
    ),
    Playlist(
        index = 1,
        label = "Playlist"
    )
}