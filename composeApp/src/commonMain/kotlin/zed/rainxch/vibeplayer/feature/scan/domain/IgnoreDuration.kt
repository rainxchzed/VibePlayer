package zed.rainxch.vibeplayer.feature.scan.domain

enum class IgnoreDuration(val value: Int) {
    LESS_THAN_30_SECONDS(30_000),
    LESS_THAN_60_SECONDS(60_000),
}