package zed.rainxch.vibeplayer.feature.songs.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import zed.rainxch.vibeplayer.core.domain.model.Music

data class SongsState(
    val scanResultState: ScanResultState = ScanResultState.Loading,
    val musics: ImmutableList<Music> = persistentListOf(),
    val miniPlayerVisible: Boolean = false
)

enum class ScanResultState {
    Loading,
    Ready
}
