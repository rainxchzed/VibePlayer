package zed.rainxch.vibeplayer.feature.scan.domain

enum class IgnoreSize(val value: Int) {
    LESS_THAN_100_KB(30),
    LESS_THAN_500_KB(60),
}