package zed.rainxch.vibeplayer.core.data.local.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            ALTER TABLE musics
            ADD COLUMN isFavourite INTEGER NOT NULL DEFAULT 0
            """.trimIndent()
        )

        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS playlists (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                coverImage TEXT,
                createdAt INTEGER NOT NULL
            )
            """.trimIndent()
        )

        connection.execSQL(
            """
            CREATE UNIQUE INDEX IF NOT EXISTS index_playlists_title 
            ON playlists(title)
            """.trimIndent()
        )

        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS playlist_music (
                playlistId INTEGER NOT NULL,
                musicId INTEGER NOT NULL,
                position INTEGER,
                PRIMARY KEY(playlistId, musicId),
                FOREIGN KEY(playlistId) REFERENCES playlists(id) ON DELETE CASCADE,
                FOREIGN KEY(musicId) REFERENCES musics(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        connection.execSQL(
            "CREATE INDEX IF NOT EXISTS index_playlist_music_playlistId ON playlist_music(playlistId)"
        )
        connection.execSQL(
            "CREATE INDEX IF NOT EXISTS index_playlist_music_musicId ON playlist_music(musicId)"
        )
    }
}