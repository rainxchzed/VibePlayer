package zed.rainxch.vibeplayer.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface VibePlayerGraph {
    @Serializable
    data object PermissionScreen : VibePlayerGraph

    @Serializable
    data object MainControllerScreen : VibePlayerGraph

    @Serializable
    data object ScanScreen : VibePlayerGraph

    @Serializable
    data object SearchScreen : VibePlayerGraph

    @Serializable
    data class NowPlayingScreen(val id: Int? = null) : VibePlayerGraph

}
