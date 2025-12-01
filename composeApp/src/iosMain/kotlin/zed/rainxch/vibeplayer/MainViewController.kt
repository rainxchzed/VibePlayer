package zed.rainxch.vibeplayer

import androidx.compose.ui.window.ComposeUIViewController
import zed.rainxch.vibeplayer.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}