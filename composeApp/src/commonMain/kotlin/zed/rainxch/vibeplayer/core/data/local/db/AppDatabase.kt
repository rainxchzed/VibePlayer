package zed.rainxch.vibeplayer.core.data.local.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity

@Database(
    entities = [MusicEntity::class],
    version = 2
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val musicDao: MusicsDao
}

expect object AppDatabaseConstructor: RoomDatabaseConstructor<AppDatabase>