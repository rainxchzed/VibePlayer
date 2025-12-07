package zed.rainxch.vibeplayer.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract val musicDao: MusicsDao
}