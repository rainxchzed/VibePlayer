package zed.rainxch.vibeplayer.feature.scan.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import zed.rainxch.vibeplayer.core.presentation.components.buttons.PrimaryButton
import zed.rainxch.vibeplayer.core.presentation.components.progressbars.ScanningProgressbar
import zed.rainxch.vibeplayer.core.presentation.components.radioGroup.RadioGroup
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.core.presentation.utils.ObserveAsEvents
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreDuration
import zed.rainxch.vibeplayer.feature.scan.domain.IgnoreSize

@Composable
fun ScanRoot(
    onShowSnackbar: (message: String) -> Unit,
    navigateBack: () -> Unit,
    viewModel: ScanViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) {
        when (it) {
            is ScanEvent.SnackbarMessage -> {
                onShowSnackbar(it.message)
            }
            ScanEvent.NavigateBack -> {
                navigateBack()
            }
        }
    }

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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(horizontal = 16.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScanningProgressbar(
                isAnimating = state.isScanning
            )

            Spacer(Modifier.height(24.dp))

            RadioGroup(
                modifier = Modifier.fillMaxWidth(),
                options = listOf("30s", "60s"),
                selectedOptionIndex = IgnoreDuration.entries.indexOf(state.ignoreDuration),
                label = "Ignore duration less than",
                onOptionSelected = {
                    onAction(ScanAction.ChangeIgnoreDuration(IgnoreDuration.entries[it]))
                }
            )
            Spacer(Modifier.height(16.dp))
            RadioGroup(
                modifier = Modifier.fillMaxWidth(),
                options = listOf("100KB", "500KB"),
                selectedOptionIndex = IgnoreSize.entries.indexOf(state.ignoreSize),
                label = "Ignore size less than",
                onOptionSelected = {
                    onAction(ScanAction.ChangeIgnoreSize(IgnoreSize.entries[it]))
                }
            )
            Spacer(Modifier.height(24.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = if (state.isScanning) "Scanning" else "Scan",
                enabled = state.isScanning.not(),
                isLoading = state.isScanning,
                onClick = {
                    onAction(ScanAction.StartScan)
                }
            )

        }
    }

}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        Surface {
            ScanScreen(
                state = ScanState(isScanning = true),
                onAction = {}
            )
        }
    }
}
