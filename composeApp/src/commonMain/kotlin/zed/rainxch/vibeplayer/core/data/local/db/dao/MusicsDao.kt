package zed.rainxch.vibeplayer.core.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity

@Dao
interface MusicsDao {
    @Query("SELECT * FROM musics ORDER BY title ASC")
    fun getMusicsFlow(): Flow<List<MusicEntity>>

    @Query("SELECT * FROM musics ORDER BY title ASC")
    suspend fun getMusics(): List<MusicEntity>

    @Query("SELECT * FROM musics WHERE id = :id")
    suspend fun getMusic(id: Int): MusicEntity?

    @Query("SELECT * FROM musics WHERE musicUrl = :path LIMIT 1")
    suspend fun getMusicByPath(path: String): MusicEntity?

    @Query("SELECT COUNT(*) FROM musics")
    suspend fun getMusicCount(): Int

    @Query("SELECT COUNT(*) FROM musics")
    fun getMusicCountFlow(): Flow<Int>

    @Query("DELETE FROM musics")
    suspend fun clearAll()

    @Delete
    suspend fun removeMusic(musicEntity: MusicEntity)

    @Upsert
    suspend fun insertMusic(musicEntity: MusicEntity)

    @Upsert
    suspend fun insertMusics(musics: List<MusicEntity>)
}