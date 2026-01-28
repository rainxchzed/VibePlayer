package zed.rainxch.vibeplayer.core.data.local.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
            INSERT OR IGNORE INTO playlists (id, title, coverImage, createdAt)
            VALUES (${AppDatabase.FAVOURITES_PLAYLIST_ID}, 'Favourites', NULL, strftime('%s','now') * 1000)
        """.trimIndent())

        db.execSQL("""
            INSERT OR IGNORE INTO playlist_music (playlistId, musicId, position)
            SELECT ${AppDatabase.FAVOURITES_PLAYLIST_ID}, id, NULL
            FROM musics
            WHERE isFavourite = 1
        """.trimIndent())
    }
}
