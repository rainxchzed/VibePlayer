package zed.rainxch.vibeplayer.feature.playlist.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import zed.rainxch.vibeplayer.core.data.local.db.dao.PlaylistDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistEntity
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistMusicCrossRef
import zed.rainxch.vibeplayer.core.data.mappers.toDomain
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.core.domain.model.PlaylistFull
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo
import zed.rainxch.vibeplayer.feature.playlist.domain.PlaylistsRepository

class DefaultPlaylistsRepository(
    val dao: PlaylistDao,
    val fileUtil: FileUtil
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

    override fun getPlaylistWithMusics(playlistId: Int): Flow<PlaylistFull> {
        return dao.getPlaylistWithMusics(playlistId).mapNotNull { it?.toDomain() }
    }

    override suspend fun renamePlaylist(playlistId: Int, changedName: String): Result<Unit> {
        return try {
            val trimmedName = changedName.trim()

            if (trimmedName.isBlank()) {
                return Result.failure(Exception("Playlist name cannot be empty"))
            }

            dao.renamePlaylist(playlistId = playlistId, changedName = changedName)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePlaylist(playlistId: Int): Result<Unit> {
        return try {
            dao.deletePlaylist(playlistId = playlistId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun changePlaylistCover(playlistId: Int, imagePath: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                dao.updatePlaylistCover(playlistId, fileUtil.getAbsolutePathFromUri(imagePath))
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun createPlaylist(name: String): Result<Int> {
        return try {
            val trimmedName = name.trim()

            if (trimmedName.isBlank()) {
                return Result.failure(Exception("Playlist name cannot be empty"))
            }

            val exists = dao.isPlaylistExists(trimmedName)
            if (exists) {
                return Result.failure(Exception("Playlist with this name already exists"))
            }

            val generatedId = dao.insertPlaylist(PlaylistEntity(title = trimmedName))
            Result.success(generatedId.toInt())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
