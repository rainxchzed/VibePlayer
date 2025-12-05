package zed.rainxch.vibeplayer.feature.main.domain.repository

import kotlinx.collections.immutable.ImmutableList
import zed.rainxch.vibeplayer.core.domain.model.Music

interface MainRepository {
    suspend fun getMusicsWithMetadata(): ImmutableList<Music>
}