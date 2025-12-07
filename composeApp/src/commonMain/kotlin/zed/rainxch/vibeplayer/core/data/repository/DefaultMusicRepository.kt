package zed.rainxch.vibeplayer.core.data.repository

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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

    /**
     * Main entry point for getting music list.
     * On first launch or manual refresh: performs full scan
     * On subsequent launches: validates cached data and returns quickly
     */
    override suspend fun getMusicsWithMetadata(
        ignoreSize: IgnoreSize,
        ignoreDuration: IgnoreDuration
    ): ImmutableList<Music> = withContext(Dispatchers.IO) {
        val cachedMusics = cacheMusicsDatasource.getMusics()

        if (cachedMusics.isEmpty()) {
            // First launch - perform full scan
            performFullScanAndCache(ignoreSize, ignoreDuration)
        } else {
            // Subsequent launches - validate cache
            validateAndGetCachedMusics(cachedMusics)
        }
    }

    /**
     * Performs a full scan of the device storage and caches results.
     * This is called on first launch or when user manually triggers rescan.
     */
    override suspend fun performFullScanAndCache(
        ignoreSize: IgnoreSize,
        ignoreDuration: IgnoreDuration,
        onProgress: (Int, String) -> Unit
    ): ImmutableList<Music> = withContext(Dispatchers.IO) {
        // Use platform-specific implementation to scan for audio files
        val scannedMusics = musicsDataStore.scanMusics()

        // Filter based on size and duration if needed
        val filteredMusics = scannedMusics.filter { music ->
            musicsDataStore.checkIfMusicExist(music)
        }

        // Clear old cache and insert new data
        cacheMusicsDatasource.clearAll()
        cacheMusicsDatasource.cacheMusics(
            filteredMusics.map { it.toMusicEntity() }
        )

        filteredMusics.toImmutableList()
    }

    /**
     * Lightweight validation: checks if cached files still exist.
     * Removes missing files from cache and returns valid music list.
     */
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

        // Remove invalid entries from cache
        if (musicsToRemove.isNotEmpty()) {
            cacheMusicsDatasource.removeMusics(musicsToRemove)
        }

        validMusics.toImmutableList()
    }

    /**
     * Check if cache needs refresh based on last scan time or user preference
     */
    suspend fun shouldPerformFullScan(): Boolean {
        val cachedMusics = cacheMusicsDatasource.getMusics()
        return cachedMusics.isEmpty()
    }

    /**
     * Get single music metadata by file path
     */
    suspend fun getMusicByPath(filePath: String): Music? = withContext(Dispatchers.IO) {
        cacheMusicsDatasource.getMusicByPath(filePath)?.toMusic()
    }
}