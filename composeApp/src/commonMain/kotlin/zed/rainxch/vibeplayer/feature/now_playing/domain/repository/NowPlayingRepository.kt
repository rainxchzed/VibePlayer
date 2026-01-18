package zed.rainxch.vibeplayer.feature.now_playing.domain.repository

import kotlinx.coroutines.flow.Flow
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.domain.model.Playlist

interface NowPlayingRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    fun getFavouriteSongsCount(): Flow<Int>
    suspend fun toggleMusicFavourite(music: Music)
    suspend fun addSongToPlaylist(music: Music, playlist: Playlist)
    suspend fun createNewPlaylist(title: String) : Playlist
}