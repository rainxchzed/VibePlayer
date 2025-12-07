package zed.rainxch.vibeplayer.feature.main.domain.repository

import kotlinx.collections.immutable.ImmutableList
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreDuration
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreSize

interface MainRepository {
    suspend fun getMusicsWithMetadata(
        ignoreSize: IgnoreSize = IgnoreSize.LESS_THAN_100_KB,
        ignoreDuration: IgnoreDuration = IgnoreDuration.LESS_THAN_30_SECONDS
    ): ImmutableList<Music>
}