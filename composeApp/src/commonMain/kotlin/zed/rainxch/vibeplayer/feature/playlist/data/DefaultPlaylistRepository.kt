package zed.rainxch.vibeplayer.feature.playlist.data

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import zed.rainxch.vibeplayer.core.data.data_source.CacheMusicsDataSource
import zed.rainxch.vibeplayer.core.data.data_source.MusicsDataStore
import zed.rainxch.vibeplayer.core.data.local.db.dao.PlaylistDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity
import zed.rainxch.vibeplayer.core.data.mappers.toDomain
import zed.rainxch.vibeplayer.core.data.mappers.toMusic
import zed.rainxch.vibeplayer.core.data.mappers.toMusicEntity
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo
import zed.rainxch.vibeplayer.core.domain.repository.MusicRepository
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreDuration
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreSize

class DefaultPlaylistsRepository(
    val dao: PlaylistDao
) : PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> {
        // TODO: Implement if needed, requires loading full music lists
        throw NotImplementedError("Use getPlaylistsInfo() for list display")
    }

    override fun getPlaylistsInfo(): Flow<List<PlaylistInfo>> {
        return dao.getPlaylistsWithCount().map { playlists ->
            playlists.map { it.toDomain() }
        }
    }
}
