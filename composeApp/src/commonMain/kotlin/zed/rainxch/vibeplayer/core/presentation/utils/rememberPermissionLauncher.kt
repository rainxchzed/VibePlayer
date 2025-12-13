package zed.rainxch.vibeplayer.core.presentation.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberPermissionLauncher(permission: Permission): PermissionLauncher

interface PermissionLauncher {
    fun launch(
        onPermission: (IsPermissionGranted) -> Unit
    )
}