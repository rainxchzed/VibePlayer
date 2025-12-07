package zed.rainxch.vibeplayer.core.data.data_source

import zed.rainxch.vibeplayer.core.data.local.db.dao.MusicsDao
import zed.rainxch.vibeplayer.core.data.local.db.entity.MusicEntity
import zed.rainxch.vibeplayer.core.data.mappers.toMusic

class CacheMusicsDataSource(
    private val musicsDao: MusicsDao,
    private val musicsDataStore: MusicsDataStore
) {
    suspend fun getMusics(): List<MusicEntity> {
        return musicsDao.getMusics()
    }

    suspend fun getMusicByPath(path: String): MusicEntity? {
        return musicsDao.getMusicByPath(path)
    }

    suspend fun cacheMusics(list: List<MusicEntity>) {
        musicsDao.insertMusics(list)
    }

    suspend fun cacheMusic(music: MusicEntity) {
        musicsDao.insertMusic(music)
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

    fun checkIfMusicExist(music: MusicEntity): Boolean {
        return musicsDataStore.checkIfMusicExist(music.toMusic())
    }

    suspend fun getMusicCount(): Int {
        return musicsDao.getMusicCount()
    }
}