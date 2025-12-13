package zed.rainxch.vibeplayer.feature.permission.presentation

sealed interface PermissionAction {
    data object OnAllowAccessClick : PermissionAction
    data object OnPermissionGranted : PermissionAction
    data object OnPermissionDenied : PermissionAction
    data object OnDialogPermissionTryAgainClick : PermissionAction
    data object OnDialogPermissionDoneClick : PermissionAction
}