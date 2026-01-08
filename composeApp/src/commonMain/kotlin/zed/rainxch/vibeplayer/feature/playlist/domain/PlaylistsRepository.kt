package zed.rainxch.vibeplayer.feature.playlist.domain

import kotlinx.coroutines.flow.Flow
import zed.rainxch.vibeplayer.core.domain.model.Playlist
import zed.rainxch.vibeplayer.core.domain.model.PlaylistInfo

interface PlaylistsRepository {
    suspend fun createTestPlaylist()
    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylistsInfo(): Flow<List<PlaylistInfo>>
}