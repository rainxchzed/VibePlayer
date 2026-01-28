package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import platform.darwin.cache_create
import zed.rainxch.vibeplayer.core.data.data_source.CacheMusicsDataSource
import zed.rainxch.vibeplayer.core.data.data_source.MusicsDataStore
import zed.rainxch.vibeplayer.core.data.local.db.initDatabase
import zed.rainxch.vibeplayer.core.data.repository.DefaultMusicRepository
import zed.rainxch.vibeplayer.core.domain.IosAudioPlayer
import zed.rainxch.vibeplayer.core.domain.MediaPlayerController
import zed.rainxch.vibeplayer.core.domain.repository.MusicRepository
import zed.rainxch.vibeplayer.core.presentation.utils.IsPermissionGranted
import zed.rainxch.vibeplayer.core.presentation.utils.Permission
import zed.rainxch.vibeplayer.core.presentation.utils.PermissionChecker
import zed.rainxch.vibeplayer.feature.songs.data.data_sources.IOSMusicsDataStore
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsViewModel

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

    single<MusicsDataStore> {
        IOSMusicsDataStore()
    }

}