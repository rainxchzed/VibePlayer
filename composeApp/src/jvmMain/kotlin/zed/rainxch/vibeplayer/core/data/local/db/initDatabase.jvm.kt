package zed.rainxch.vibeplayer.core.data.local.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import zed.rainxch.vibeplayer.core.data.local.db.migrations.MIGRATION_2_3
import java.io.File

fun initDatabase(): AppDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room
        .databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
        .addMigrations(MIGRATION_2_3)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}