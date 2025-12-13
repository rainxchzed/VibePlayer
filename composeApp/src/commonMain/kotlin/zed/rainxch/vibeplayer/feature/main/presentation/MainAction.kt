package zed.rainxch.vibeplayer.feature.main.presentation

import zed.rainxch.vibeplayer.core.domain.model.Music

sealed interface MainAction {
    data object OnScanAgainClick : MainAction
    data class OnMusicItemClick(val music: Music) : MainAction
}