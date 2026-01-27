package zed.rainxch.vibeplayer.app.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import zed.rainxch.vibeplayer.AppViewModel
import zed.rainxch.vibeplayer.core.data.data_source.CacheMusicsDataSource
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.core.data.local.db.dao.PlaylistDao
import zed.rainxch.vibeplayer.core.data.repository.DefaultMusicRepository
import zed.rainxch.vibeplayer.core.domain.repository.MusicRepository
import zed.rainxch.vibeplayer.feature.now_playing.data.repository.NowPlayingRepositoryImpl
import zed.rainxch.vibeplayer.feature.now_playing.domain.repository.NowPlayingRepository
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackViewModel
import zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation.AddSongsViewModel
import zed.rainxch.vibeplayer.feature.playlist.data.DefaultPlaylistsRepository
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository
import zed.rainxch.vibeplayer.feature.playlist.presentation.PlaylistViewModel
import zed.rainxch.vibeplayer.feature.playlistPlayback.PlaylistPlaybackViewModel
import zed.rainxch.vibeplayer.feature.scan.presentation.ScanViewModel
import zed.rainxch.vibeplayer.feature.search.data.repository.SearchRepositoryImpl
import zed.rainxch.vibeplayer.feature.search.domain.repository.SearchRepository
import zed.rainxch.vibeplayer.feature.search.presentation.SearchViewModel
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsViewModel

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::ScanViewModel)
    viewModelOf(::SongsViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::PlaylistViewModel)
    viewModelOf(::AddSongsViewModel)
    viewModelOf(::PlaylistPlaybackViewModel)

    single {
        MusicPlaybackViewModel(
            playerController = get(),
            nowPlayingRepository = get()
        )
    }

    single<MusicRepository> {
        DefaultMusicRepository(
            cacheMusicsDatasource = get(),
            musicsDataStore = get()
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(
            cacheMusicsDatasource = get(),
        )
    }

    single<PlaylistsRepository> {
        DefaultPlaylistsRepository(
            dao = get(),
            fileUtil = get()
        )
    }

    single<NowPlayingRepository> {
        NowPlayingRepositoryImpl(
            playlistDao = get()
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

    single<PlaylistDao> {
        get<AppDatabase>().playlistDao
    }
}