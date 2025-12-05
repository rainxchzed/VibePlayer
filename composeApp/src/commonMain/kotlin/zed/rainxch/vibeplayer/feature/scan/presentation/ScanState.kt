package zed.rainxch.vibeplayer.feature.scan.presentation

import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreDuration
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreSize

data class ScanState(
    val isScanning: Boolean = false,
    val ignoreSize: IgnoreSize = IgnoreSize.LESS_THAN_100_KB,
    val ignoreDuration: IgnoreDuration = IgnoreDuration.LESS_THAN_30_SECONDS
)