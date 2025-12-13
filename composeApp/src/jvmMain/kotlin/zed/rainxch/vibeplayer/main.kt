package zed.rainxch.vibeplayer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import zed.rainxch.vibeplayer.app.di.initKoin

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "VibePlayer",
    ) {
        App()
    }
}