package zed.rainxch.vibeplayer.core.data.mappers

import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity
import zed.rainxch.vibeplayer.core.domain.model.Music

fun MusicEntity.toMusic(): Music {
    return Music(
        id = id,
        title = this.title,
        duration = this.duration,
        artist = this.artist,
        bannerUrl = this.bannerUrl,
        musicUrl = this.musicUrl,
    )
}

fun Music.toMusicEntity(): MusicEntity {
    return MusicEntity(
        id = id,
        title = this.title,
        duration = this.duration,
        artist = this.artist,
        bannerUrl = this.bannerUrl,
        musicUrl = this.musicUrl,
    )
}