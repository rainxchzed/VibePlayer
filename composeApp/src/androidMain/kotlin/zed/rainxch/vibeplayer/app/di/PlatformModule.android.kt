package zed.rainxch.vibeplayer.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import zed.rainxch.vibeplayer.core.presentation.utils.AndroidAudioPermission
import zed.rainxch.vibeplayer.core.presentation.utils.AudioPermission

actual val platformModule: Module = module {
    single<AudioPermission> {
        AndroidAudioPermission(androidContext())
    }
}