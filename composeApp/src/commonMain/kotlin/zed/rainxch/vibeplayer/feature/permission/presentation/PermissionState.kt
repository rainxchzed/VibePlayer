package zed.rainxch.vibeplayer.feature.permission.presentation

data class PermissionState(
    val didDenyPermission: Boolean = false,
    val isPermissionDeniedDialogVisible: Boolean = false,
)