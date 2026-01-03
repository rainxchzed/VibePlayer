package zed.rainxch.vibeplayer.core.data.local.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_2_3 = object : Migration(startVersion = 2, endVersion = 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            ALTER TABLE musics
            ADD COLUMN isFavourite INTEGER NOT NULL DEFAULT 0
        """.trimIndent())
    }
}