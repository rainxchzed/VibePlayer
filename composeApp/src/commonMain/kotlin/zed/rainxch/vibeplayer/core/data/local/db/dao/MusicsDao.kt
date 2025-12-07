package zed.rainxch.vibeplayer.core.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity

@Dao
interface MusicsDao {
    @Query("SELECT * FROM musics")
    suspend fun getMusics(): List<MusicEntity>
}