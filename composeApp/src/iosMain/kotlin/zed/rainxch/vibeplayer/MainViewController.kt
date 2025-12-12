package zed.rainxch.vibeplayer

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import zed.rainxch.vibeplayer.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
    configureAudioSession()
}

@OptIn(ExperimentalForeignApi::class)
fun configureAudioSession() {
    val session = AVAudioSession.sharedInstance()
    try {
        session.setCategory(AVAudioSessionCategoryPlayback, error = null)
        session.setActive(true, error = null)
    } catch (e: Exception) {
        println("Failed to setup audio session: ${e.message}")
    }
}