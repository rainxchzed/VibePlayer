package zed.rainxch.vibeplayer.core.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("musics")
data class MusicEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val duration: String,
    val artist: String,
    val bannerUrl: String? = null,
    val musicUrl: String
)