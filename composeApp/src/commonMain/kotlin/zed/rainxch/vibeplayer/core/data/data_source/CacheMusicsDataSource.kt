package zed.rainxch.vibeplayer.core.data.data_source

import kotlinx.coroutines.flow.Flow
import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity

class CacheMusicsDataSource(
    private val musicsDao: MusicsDao
) {
    fun getMusicsFlow(): Flow<List<MusicEntity>> {
        return musicsDao.getMusicsFlow()
    }

    suspend fun getMusics(): List<MusicEntity> {
        return musicsDao.getMusics()
    }

    fun getMusicCountFlow(): Flow<Int> {
        return musicsDao.getMusicCountFlow()
    }

    suspend fun getMusicByPath(path: String): MusicEntity? {
        return musicsDao.getMusicByPath(path)
    }

    suspend fun cacheMusics(list: List<MusicEntity>) {
        musicsDao.insertMusics(list)
    }

    suspend fun removeMusic(music: MusicEntity) {
        musicsDao.removeMusic(music)
    }

    suspend fun removeMusics(musics: List<MusicEntity>) {
        musics.forEach { musicsDao.removeMusic(it) }
    }

    suspend fun clearAll() {
        musicsDao.clearAll()
    }

}