package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import zed.rainxch.vibeplayer.AppViewModel
import zed.rainxch.vibeplayer.core.data.data_source.CacheMusicsDataSource
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.core.data.repository.DefaultMusicRepository
import zed.rainxch.vibeplayer.core.domain.repository.MusicRepository
import zed.rainxch.vibeplayer.feature.main.presentation.MainViewModel
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackViewModel
import zed.rainxch.vibeplayer.feature.scan.presentation.ScanViewModel

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::ScanViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicPlaybackViewModel)


    single<MusicRepository> {
        DefaultMusicRepository(
            cacheMusicsDatasource = get(),
            musicsDataStore = get()
        )
    }

    single<CacheMusicsDataSource> {
        CacheMusicsDataSource(
            musicsDao = get()
        )
    }

    single<MusicsDao> {
        get<AppDatabase>().musicDao
    }
}