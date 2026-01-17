package zed.rainxch.vibeplayer.feature.playlist.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import zed.rainxch.vibeplayer.core.data.local.db.dao.PlaylistDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistEntity
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistMusicCrossRef
import zed.rainxch.vibeplayer.core.data.mappers.toDomain
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository

class DefaultPlaylistsRepository(
    val dao: PlaylistDao
) : PlaylistsRepository {

    override suspend fun addSongsToPlaylist(
        playlistId: Int,
        songIds: List<Int>
    ): Result<Unit> {
        return withContext(Dispatchers.IO) { // Perform database operations on a background thread
            try {
                val crossRefs = songIds.map { songId ->
                    PlaylistMusicCrossRef(playlistId = playlistId, musicId = songId)
                }
                dao.addMusicsToPlaylist(crossRefs = crossRefs)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        // TODO: Implement if needed, requires loading full music lists
        throw NotImplementedError("Use getPlaylistsInfo() for list display")
    }

    override fun getPlaylistsInfo(): Flow<List<PlaylistInfo>> {
        return dao.getPlaylistsWithCount().map { playlists ->
            playlists.map { it.toDomain() }
        }
    }

    override suspend fun createPlaylist(name: String): Result<Unit> {
        return try {
            val trimmedName = name.trim()

            if (trimmedName.isBlank()) {
                return Result.failure(Exception("Playlist name cannot be empty"))
            }

            val exists = dao.isPlaylistExists(trimmedName)
            if (exists) {
                return Result.failure(Exception("Playlist with this name already exists"))
            }

            dao.insertPlaylist(PlaylistEntity(title = trimmedName))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
