package zed.rainxch.vibeplayer.core.presentation.utils

typealias IsPermissionGranted = Boolean

interface PermissionChecker {
    fun isPermissionGranted(permission: Permission): IsPermissionGranted
}

enum class Permission {
    Audio;
}