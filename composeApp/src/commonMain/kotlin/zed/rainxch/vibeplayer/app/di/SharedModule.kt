package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import zed.rainxch.vibeplayer.AppViewModel
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.feature.main.data.data_source.CacheMusicsDataSource
import zed.rainxch.vibeplayer.feature.main.data.repository.DefaultMainRepository
import zed.rainxch.vibeplayer.feature.main.domain.repository.MainRepository
import zed.rainxch.vibeplayer.feature.main.presentation.MainViewModel
import zed.rainxch.vibeplayer.feature.scan.presentation.ScanViewModel

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::ScanViewModel)
    viewModelOf(::MainViewModel)

    single<MainRepository> {
        DefaultMainRepository(
            cacheMusicsDatasource = get(),
            musicsDataStore = get()
        )
    }

    single<CacheMusicsDataSource> {
        CacheMusicsDataSource(
            musicsDataStore = get(),
            musicsDao = get()
        )
    }

    single<MusicsDao> {
        get<AppDatabase>().musicDao
    }
}