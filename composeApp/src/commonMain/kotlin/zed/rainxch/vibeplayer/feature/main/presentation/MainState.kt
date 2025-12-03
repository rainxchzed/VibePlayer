package zed.rainxch.vibeplayer.feature.main.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import zed.rainxch.vibeplayer.core.domain.model.Music

data class MainState(
    val scanResultState: ScanResultState = ScanResultState.Loading,
    val musics: ImmutableList<Music> = persistentListOf()
)

enum class ScanResultState {
    Loading,
    Ready
}