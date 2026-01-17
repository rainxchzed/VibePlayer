@file:OptIn(ExperimentalTime::class)

package zed.rainxch.vibeplayer.core.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "playlists",
    indices = [
        Index(value = ["title"], unique = true)
    ]
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "coverImage")
    val coverImage: String? = null,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)
