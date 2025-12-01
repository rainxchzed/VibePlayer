package zed.rainxch.vibeplayer

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import zed.rainxch.vibeplayer.app.navigation.AppNavigation
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
@Preview
fun App() {
    VibePlayerTheme {
        AppNavigation()
    }
}