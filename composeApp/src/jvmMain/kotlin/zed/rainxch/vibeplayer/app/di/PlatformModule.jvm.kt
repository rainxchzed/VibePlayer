package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.Module
import org.koin.dsl.module
import zed.rainxch.vibeplayer.core.presentation.utils.AudioPermission
import zed.rainxch.vibeplayer.core.presentation.utils.JvmAudioPermission

actual val platformModule: Module= module {
    single<AudioPermission> {
        JvmAudioPermission()
    }
}