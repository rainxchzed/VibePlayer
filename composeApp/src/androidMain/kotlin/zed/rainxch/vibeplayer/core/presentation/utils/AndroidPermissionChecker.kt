package zed.rainxch.vibeplayer.core.presentation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class AndroidPermissionChecker(
    private val context: Context
) : PermissionChecker {
    override fun isPermissionGranted(permission: Permission): IsPermissionGranted {
        return context.checkSelfPermission(
            permission.toManifestPermissionCode()
        ) == PackageManager.PERMISSION_GRANTED
    }
}

fun Permission.toManifestPermissionCode(): String {
    return when (this) {
        Permission.Audio -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }
    }
}