package zed.rainxch.vibeplayer.feature.search.data.repository

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import zed.rainxch.vibeplayer.core.data.data_source.CacheMusicsDataSource
import zed.rainxch.vibeplayer.core.data.mappers.toMusic
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.feature.search.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val cacheMusicsDatasource: CacheMusicsDataSource,
) : SearchRepository {
    override suspend fun performSearch(query: String): ImmutableList<Music> {
        return cacheMusicsDatasource
            .searchMusics(query)
            .map { it.toMusic() }
            .toImmutableList()
    }
}