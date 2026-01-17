package zed.rainxch.vibeplayer.core.data.local.db

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import zed.rainxch.vibeplayer.core.data.local.db.migrations.MIGRATION_2_3

fun initDatabase(context: Context): AppDatabase {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("my_room.db")
    return Room
        .databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
        .addMigrations(MIGRATION_2_3)
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}