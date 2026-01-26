package zed.rainxch.vibeplayer.core.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import zed.rainxch.vibeplayer.core.data.local.db.migrations.MIGRATION_2_3
import zed.rainxch.vibeplayer.core.data.local.db.migrations.MIGRATION_3_4

fun initDatabase(context: Context): AppDatabase {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")

    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL("""
                    INSERT INTO playlists (id, title, coverImage, createdAt)
                    VALUES (${AppDatabase.FAVOURITES_PLAYLIST_ID}, 'Favourites', NULL, strftime('%s','now') * 1000)
                """.trimIndent())
            }
        })
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
