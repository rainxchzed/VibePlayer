package zed.rainxch.vibeplayer.core.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import zed.rainxch.vibeplayer.core.domain.model.MusicId

@Entity(
    tableName = "playlist_music",
    primaryKeys = ["playlistId", "musicId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = ["id"],
            childColumns = ["musicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("playlistId"),
        Index("musicId")
    ]
)
data class PlaylistMusicCrossRef(
    val playlistId: Int,
    val musicId: MusicId,
    val position: Int? = null
)
