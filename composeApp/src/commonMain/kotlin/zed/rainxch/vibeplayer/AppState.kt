package zed.rainxch.vibeplayer

data class AppState(
    val isAudioPermissionGranted: Boolean = false,
    val isLoading: Boolean = true
)
