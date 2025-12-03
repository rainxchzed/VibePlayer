package zed.rainxch.vibeplayer.feature.scan.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun ScanRoot(
    viewModel: ScanViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScanScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ScanScreen(
    state: ScanState,
    onAction: (ScanAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        ScanScreen(
            state = ScanState(),
            onAction = {}
        )
    }
}