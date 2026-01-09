package zed.rainxch.vibeplayer.core.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistEntity
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistMusicCrossRef
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistWithCount
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistWithMusics
import zed.rainxch.vibeplayer.core.domain.model.MusicId

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query(
        """
        SELECT p.*, COUNT(pm.musicId) as musicCount
        FROM playlists p
        LEFT JOIN playlist_music pm ON p.id = pm.playlistId
        GROUP BY p.id
        ORDER BY p.createdAt DESC
        """
    )
    fun getPlaylistsWithCount(): Flow<List<PlaylistWithCount>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistWithMusics(
        playlistId: Int
    ): Flow<PlaylistWithMusics>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMusicToPlaylist(
        crossRef: PlaylistMusicCrossRef
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMusicsToPlaylist(
        crossRefs: List<PlaylistMusicCrossRef>
    )

    @Query(
        """
        DELETE FROM playlist_music
        WHERE playlistId = :playlistId AND musicId = :musicId
        """
    )
    suspend fun removeMusicFromPlaylist(
        playlistId: Int,
        musicId: MusicId
    )

    @Query(
        "DELETE FROM playlist_music WHERE playlistId = :playlistId"
    )
    suspend fun clearPlaylist(
        playlistId: Int
    )

    @Query("SELECT EXISTS(SELECT 1 FROM playlists WHERE title = :title LIMIT 1)")
    suspend fun isPlaylistExists(title: String): Boolean
}
