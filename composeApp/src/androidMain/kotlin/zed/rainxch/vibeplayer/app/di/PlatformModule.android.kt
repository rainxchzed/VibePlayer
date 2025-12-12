package zed.rainxch.vibeplayer.app.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import zed.rainxch.vibeplayer.AndroidAudioPlayer
import zed.rainxch.vibeplayer.core.data.data_source.MusicsDataStore
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.core.data.local.db.initDatabase
import zed.rainxch.vibeplayer.core.domain.MediaPlayerController
import zed.rainxch.vibeplayer.core.presentation.utils.AndroidPermissionChecker
import zed.rainxch.vibeplayer.core.presentation.utils.PermissionChecker
import zed.rainxch.vibeplayer.feature.main.data.data_sources.AndroidMusicsDataStore

actual val platformModule: Module = module {
    single<PermissionChecker> {
        AndroidPermissionChecker(androidContext())
    }

    single<AppDatabase> {
        initDatabase(androidContext())
    }

    single<MusicsDataStore> {
        AndroidMusicsDataStore(androidContext())
    }

    single<MediaPlayerController>{
        AndroidAudioPlayer(context = androidContext())
    }
}