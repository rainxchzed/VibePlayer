package zed.rainxch.vibeplayer.core.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(
    tableName = "musics",
    indices = [Index(value = ["musicUrl"], unique = true)]
)
data class MusicEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "duration")
    val duration: String,
    @ColumnInfo(name = "artist")
    val artist: String,
    @ColumnInfo(name = "bannerUrl")
    val bannerUrl: String?,
    @ColumnInfo(name = "musicUrl")
    val musicUrl: String,
    @ColumnInfo(name = "addedAt")
    val addedAt: Long = Clock.System.now().toEpochMilliseconds()
)