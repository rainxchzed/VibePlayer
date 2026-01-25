package zed.rainxch.vibeplayer.feature.playlist.domain

import kotlinx.coroutines.flow.Flow
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistWithMusics
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.core.domain.model.PlaylistFull
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo

interface PlaylistsRepository {
    suspend fun createPlaylist(name: String): Result<Unit>
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistsInfo(): Flow<List<PlaylistInfo>>
    fun getPlaylistWithMusics(playlistId: Int): Flow<PlaylistFull>

    suspend fun addSongsToPlaylist(playlistId: Int, songIds: List<Int>): Result<Unit>

}