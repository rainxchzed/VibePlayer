package zed.rainxch.vibeplayer.feature.scan.presentation

import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreDuration
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreSize

sealed interface ScanAction {
    data class ChangeIgnoreSize(val ignoreSize: IgnoreSize) : ScanAction
    data class ChangeIgnoreDuration(val ignoreDuration: IgnoreDuration) : ScanAction
    data object StartScan: ScanAction
}