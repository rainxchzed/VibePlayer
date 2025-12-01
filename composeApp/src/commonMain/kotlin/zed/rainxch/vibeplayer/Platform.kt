package zed.rainxch.vibeplayer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform