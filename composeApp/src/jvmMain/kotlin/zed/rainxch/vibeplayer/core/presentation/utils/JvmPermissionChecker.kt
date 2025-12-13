package zed.rainxch.vibeplayer.core.presentation.utils

class JvmPermissionChecker : PermissionChecker {
    override fun isPermissionGranted(permission: Permission): Boolean = true
}
