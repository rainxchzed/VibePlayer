package zed.rainxch.vibeplayer.core.presentation.utils

typealias IsPermissionGranted = Boolean

interface AudioPermission {
    fun isAudioPermissionGranted(): IsPermissionGranted
}