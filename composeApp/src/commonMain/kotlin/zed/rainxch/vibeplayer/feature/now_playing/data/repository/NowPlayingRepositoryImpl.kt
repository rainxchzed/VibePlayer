package zed.rainxch.vibeplayer.feature.now_playing.data.repository

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.core.data.local.db.dao.PlaylistDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistEntity
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistMusicCrossRef
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.feature.now_playing.domain.repository.NowPlayingRepository

class NowPlayingRepositoryImpl(
    private val playlistDao: PlaylistDao
) : NowPlayingRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists()
            .flatMapLatest { playlists ->
                if (playlists.isEmpty()) {
                    return@flatMapLatest kotlinx.coroutines.flow.flowOf(emptyList())
                }
                val flows = playlists.map { playlistEntity ->
                    playlistDao
                        .getPlaylistWithMusics(playlistEntity.id)
                        .map { playlistWithMusics ->
                            Playlist(
                                id = playlistEntity.id,
                                title = playlistEntity.title,
                                musics = playlistWithMusics?.musics
                                    ?.map { it.id }
                                    ?.toImmutableList() ?: emptyList<Int>().toImmutableList(),
                                coverImage = playlistEntity.coverImage
                            )
                        }
                }
                combine(flows) { it.toList().toImmutableList() }
            }
            .flowOn(Dispatchers.Default)
    }

    override suspend fun addFavouriteSong(musicId: Int) {
        playlistDao.addMusicToPlaylist(
            PlaylistMusicCrossRef(
                playlistId = AppDatabase.FAVOURITES_PLAYLIST_ID,
                musicId = musicId
            )
        )
    }

    override suspend fun removeFavouriteSong(musicId: Int) {
        playlistDao.removeMusicFromPlaylist(
            AppDatabase.FAVOURITES_PLAYLIST_ID,
            musicId
        )
    }

    override suspend fun addSongToPlaylist(
        musicId: Int,
        playlistId: Int
    ) {
        playlistDao.addMusicToPlaylist(
            crossRef = PlaylistMusicCrossRef(
                musicId = musicId,
                playlistId = playlistId
            )
        )
    }

    override suspend fun removeSongFromPlaylist(
        musicId: Int,
        playlistId: Int
    ) {
        playlistDao.removeMusicFromPlaylist(
            musicId = musicId,
            playlistId = playlistId
        )
    }

    override suspend fun createNewPlaylist(title: String): Playlist {

        val entity = PlaylistEntity(
            id = 0,
            title = title,
            coverImage = null,
        )

        val generatedId = playlistDao.insertPlaylist(entity)

        return Playlist(
            id = generatedId.toInt(),
            title = title,
            coverImage = null,
            musics = persistentListOf()
        )
    }

    override suspend fun isMusicFavourite(musicId: Int): Boolean {
        return playlistDao.isMusicInPlaylist(
            playlistId = AppDatabase.FAVOURITES_PLAYLIST_ID,
            musicId = musicId
        )
    }
}