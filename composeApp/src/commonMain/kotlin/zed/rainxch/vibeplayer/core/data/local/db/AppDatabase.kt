package zed.rainxch.vibeplayer.core.data.local.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.core.data.local.db.dao.PlaylistDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistEntity
import zed.rainxch.vibeplayer.core.data.local.db.entity.PlaylistMusicCrossRef

@Database(
    entities = [
        MusicEntity::class,
        PlaylistEntity::class,
        PlaylistMusicCrossRef::class
    ],
    version = 4
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val musicDao: MusicsDao
    abstract val playlistDao: PlaylistDao

    companion object {
        const val FAVOURITES_PLAYLIST_ID = 0
    }
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>