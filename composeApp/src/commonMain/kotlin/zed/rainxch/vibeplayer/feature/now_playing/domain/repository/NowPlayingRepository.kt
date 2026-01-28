package zed.rainxch.vibeplayer.feature.now_playing.domain.repository

import kotlinx.coroutines.flow.Flow
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.domain.model.Playlist

interface NowPlayingRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addFavouriteSong(musicId: Int)
    suspend fun removeFavouriteSong(musicId: Int)
    suspend fun addSongToPlaylist(musicId: Int, playlistId: Int)
    suspend fun removeSongFromPlaylist(musicId: Int, playlistId: Int)
    suspend fun createNewPlaylist(title: String) : Playlist
    suspend fun isMusicFavourite(musicId: Int) : Boolean
}