package zed.rainxch.vibeplayer.feature.playlist.domain

import kotlinx.coroutines.flow.Flow
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.core.domain.model.PlaylistFull
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo

interface PlaylistsRepository {
    suspend fun createPlaylist(name: String): Result<Int>
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistsInfo(): Flow<List<PlaylistInfo>>
    fun getPlaylistWithMusics(playlistId: Int): Flow<PlaylistFull>

    suspend fun addSongsToPlaylist(playlistId: Int, songIds: List<Int>): Result<Unit>
    suspend fun renamePlaylist(playlistId: Int, changedName: String): Result<Unit>


}