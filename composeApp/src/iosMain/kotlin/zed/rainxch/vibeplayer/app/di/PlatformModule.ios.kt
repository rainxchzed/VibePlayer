package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.Module
import org.koin.dsl.module
import zed.rainxch.vibeplayer.core.data.local.db.initDatabase
import zed.rainxch.vibeplayer.core.domain.IosAudioPlayer
import zed.rainxch.vibeplayer.core.domain.MediaPlayerController
import zed.rainxch.vibeplayer.core.presentation.utils.IsPermissionGranted
import zed.rainxch.vibeplayer.core.presentation.utils.Permission
import zed.rainxch.vibeplayer.core.presentation.utils.PermissionChecker

actual val platformModule: Module = module {
    single {
        initDatabase()
    }

    single<MediaPlayerController>{ IosAudioPlayer() }

    single<PermissionChecker> {
        object : PermissionChecker {

            override fun isPermissionGranted(permission: Permission): IsPermissionGranted {
                return true
            }
        }
    }

}