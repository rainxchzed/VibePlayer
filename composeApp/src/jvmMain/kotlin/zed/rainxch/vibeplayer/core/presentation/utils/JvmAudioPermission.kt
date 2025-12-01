package zed.rainxch.vibeplayer.core.presentation.utils

class JvmAudioPermission : AudioPermission {
    override fun isAudioPermissionGranted(): Boolean = true
}
