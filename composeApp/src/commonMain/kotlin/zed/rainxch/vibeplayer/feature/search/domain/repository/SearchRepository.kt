package zed.rainxch.vibeplayer.feature.search.domain.repository

import kotlinx.collections.immutable.ImmutableList
import zed.rainxch.vibeplayer.core.domain.model.Music

interface SearchRepository {
    suspend fun performSearch(query: String): ImmutableList<Music>
}