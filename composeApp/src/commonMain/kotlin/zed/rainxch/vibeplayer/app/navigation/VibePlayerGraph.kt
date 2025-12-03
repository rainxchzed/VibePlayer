package zed.rainxch.vibeplayer.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface VibePlayerGraph {
    @Serializable
    data object PermissionScreen : VibePlayerGraph

    @Serializable
    data object MainScreen : VibePlayerGraph
    @Serializable
    data object ScanScreen : VibePlayerGraph
}