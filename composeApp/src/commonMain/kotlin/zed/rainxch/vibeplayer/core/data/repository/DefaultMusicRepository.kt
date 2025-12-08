package zed.rainxch.vibeplayer.core.data.repository

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity
import zed.rainxch.vibeplayer.core.data.mappers.toMusic
import zed.rainxch.vibeplayer.core.data.mappers.toMusicEntity
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.data.data_source.CacheMusicsDataSource
import zed.rainxch.vibeplayer.core.data.data_source.MusicsDataStore
import zed.rainxch.vibeplayer.core.domain.repository.MusicRepository
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreDuration
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreSize

class DefaultMusicRepository(
    private val cacheMusicsDatasource: CacheMusicsDataSource,
    private val musicsDataStore: MusicsDataStore
) : MusicRepository {

    override fun getMusicsWithMetadataFlow(
        ignoreSize: IgnoreSize,
        ignoreDuration: IgnoreDuration
    ): Flow<ImmutableList<Music>> = cacheMusicsDatasource.getMusicsFlow()
        .map { entities ->
            entities.mapNotNull { entity ->
                val music = entity.toMusic()
                if (musicsDataStore.checkIfMusicExist(music)) {
                    music
                } else {
                    cacheMusicsDatasource.removeMusic(entity)
                    null
                }
            }.toImmutableList()
        }
        .flowOn(Dispatchers.IO)

    override suspend fun getMusicsWithMetadata(
        ignoreSize: IgnoreSize,
        ignoreDuration: IgnoreDuration
    ): ImmutableList<Music> = withContext(Dispatchers.IO) {
        val cachedMusics = cacheMusicsDatasource.getMusics()

        if (cachedMusics.isEmpty()) {
            performFullScanAndCache(ignoreSize, ignoreDuration)
        } else {
            validateAndGetCachedMusics(cachedMusics)
        }
    }

    override suspend fun performFullScanAndCache(
        ignoreSize: IgnoreSize,
        ignoreDuration: IgnoreDuration,
        onProgress: (Int, String) -> Unit
    ): ImmutableList<Music> = withContext(Dispatchers.IO) {
        val scannedMusics = musicsDataStore.scanMusics()

        val filteredMusics = scannedMusics.filter { music ->
            musicsDataStore.checkIfMusicExist(music)
        }

        cacheMusicsDatasource.clearAll()
        cacheMusicsDatasource.cacheMusics(
            filteredMusics.map { it.toMusicEntity() }
        )

        filteredMusics.toImmutableList()
    }

    private suspend fun validateAndGetCachedMusics(
        cachedMusics: List<MusicEntity>
    ): ImmutableList<Music> = withContext(Dispatchers.IO) {
        val validMusics = mutableListOf<Music>()
        val musicsToRemove = mutableListOf<MusicEntity>()

        cachedMusics.forEach { entity ->
            val music = entity.toMusic()
            if (musicsDataStore.checkIfMusicExist(music)) {
                validMusics.add(music)
            } else {
                musicsToRemove.add(entity)
            }
        }

        if (musicsToRemove.isNotEmpty()) {
            cacheMusicsDatasource.removeMusics(musicsToRemove)
        }

        validMusics.toImmutableList()
    }
}
