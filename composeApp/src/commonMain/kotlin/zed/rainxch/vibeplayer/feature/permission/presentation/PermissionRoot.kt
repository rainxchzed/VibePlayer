package zed.rainxch.vibeplayer.feature.permission.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.app_logo
import zed.rainxch.vibeplayer.core.presentation.components.buttons.PrimaryButton
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.core.presentation.utils.Permission
import zed.rainxch.vibeplayer.core.presentation.utils.rememberPermissionLauncher
import zed.rainxch.vibeplayer.feature.permission.presentation.components.PermissionDeniedDialog

@Composable
fun PermissionRoot(
    onNavigateToMain: () -> Unit,
    viewModel: PermissionViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val permissionLauncher = rememberPermissionLauncher(Permission.Audio)

    if (state.isPermissionDeniedDialogVisible) {
        PermissionDeniedDialog(
            onDismiss = {
                viewModel.onAction(PermissionAction.OnDialogPermissionDoneClick)
            },
            onTryAgainClick = {
                viewModel.onAction(PermissionAction.OnDialogPermissionTryAgainClick)
            }
        )
    }

    PermissionScreen(
        state = state,
        onAction = { action ->
            when (action) {
                PermissionAction.OnPermissionGranted -> {
                    onNavigateToMain()
                }

                PermissionAction.OnAllowAccessClick -> {
                    permissionLauncher.launch { isGranted ->
                        if (isGranted) {
                            onNavigateToMain()
                        } else {
                            viewModel.onAction(PermissionAction.OnPermissionDenied)
                        }
                    }
                }

                PermissionAction.OnDialogPermissionTryAgainClick -> {
                    permissionLauncher.launch { isGranted ->
                        if (isGranted) {
                            viewModel.onAction(PermissionAction.OnPermissionGranted)
                        } else {
                            viewModel.onAction(PermissionAction.OnPermissionDenied)
                        }
                    }
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}

@Composable
fun PermissionScreen(
    state: PermissionState,
    onAction: (PermissionAction) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size(56.dp),
        )

        Spacer(Modifier.height(28.dp))

        Text(
            text = "VibePlayer",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = if (state.didDenyPermission) {
                "VibePlayer needs access to your music files to function properly. Without this permission, the app cannot build your music library or play songs."
            } else "VibePlayer needs access to your music files to build your library and play songs",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            text = "Allow Access",
            onClick = {
                onAction(PermissionAction.OnAllowAccessClick)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        PermissionScreen(
            state = PermissionState(),
            onAction = {}
        )
    }
}