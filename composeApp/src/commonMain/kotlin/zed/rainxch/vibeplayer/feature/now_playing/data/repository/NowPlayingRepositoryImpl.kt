package zed.rainxch.vibeplayer.feature.now_playing.data.repository

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.core.data.local.db.dao.PlaylistDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistEntity
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistMusicCrossRef
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.feature.now_playing.domain.repository.NowPlayingRepository
import kotlin.random.Random

class NowPlayingRepositoryImpl(
    private val musicsDao: MusicsDao,
    private val playlistDao: PlaylistDao
) : NowPlayingRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists()
            .flatMapLatest { playlists ->
                val flows = playlists.map { playlistEntity ->
                    playlistDao
                        .getPlaylistWithMusics(playlistEntity.id)
                        .map { playlistWithMusics ->
                            Playlist(
                                id = playlistEntity.id,
                                title = playlistEntity.title,
                                musics = playlistWithMusics.musics
                                    .map { it.id }
                                    .toImmutableList(),
                                coverImage = playlistEntity.coverImage
                            )
                        }
                }
                combine(flows) { it.toImmutableList() }
            }
            .flowOn(Dispatchers.Default)
    }

    override fun getFavouriteSongsCount(): Flow<Int> {
        return musicsDao
            .getFavouriteMusicsFlow()
            .map { it.count() }
    }

    override suspend fun toggleMusicFavourite(music: Music) {
        val newStatus = !music.isFavourite
        musicsDao.updateFavouriteStatus(music.id, newStatus)
    }

    override suspend fun addSongToPlaylist(
        music: Music,
        playlist: Playlist
    ) {
        playlistDao.addMusicToPlaylist(
            PlaylistMusicCrossRef(
                playlistId = playlist.id,
                musicId = music.id
            )
        )
    }

    override suspend fun createNewPlaylist(title: String): Playlist {
        val newId = Random.nextInt(Int.MAX_VALUE)

        val newPlaylist = Playlist(
            id = newId,
            title = title,
            coverImage = null,
            musics = persistentListOf()
        )

        playlistDao.insertPlaylist(
            PlaylistEntity(
                id = newId,
                title = title,
                coverImage = null,
            )
        )

        return newPlaylist
    }
}