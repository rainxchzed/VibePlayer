package zed.rainxch.vibeplayer.core.presentation.utils

import androidx.compose.runtime.Composable

@Composable
actual fun rememberPermissionLauncher(permission: Permission): PermissionLauncher {
    return object : PermissionLauncher {
        override fun launch(onPermission: (IsPermissionGranted) -> Unit) {
            onPermission(true)
        }
    }
}