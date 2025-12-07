package zed.rainxch.vibeplayer.feature.main.data.data_source

import kotlinx.collections.immutable.ImmutableList
import zed.rainxch.vibeplayer.core.domain.model.Music

interface MusicsDataStore {
    fun scanMusics(): ImmutableList<Music>
    fun checkIfMusicExist(music: Music) : Boolean
}