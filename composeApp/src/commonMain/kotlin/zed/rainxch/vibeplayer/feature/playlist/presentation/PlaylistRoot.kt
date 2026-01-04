package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun PlaylistRoot(
    viewModel: PlaylistViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlaylistScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PlaylistScreen(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
) {
    Text(
        text = "Hi :)",
        color = Color.White
    )
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        PlaylistScreen(
            state = PlaylistState(),
            onAction = {}
        )
    }
}