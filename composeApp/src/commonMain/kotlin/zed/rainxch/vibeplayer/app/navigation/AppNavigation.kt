package zed.rainxch.vibeplayer.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import org.koin.compose.viewmodel.koinViewModel
import zed.rainxch.vibeplayer.AppViewModel
import zed.rainxch.vibeplayer.feature.permission.presentation.PermissionRoot

@Composable
fun AppNavigation(
    viewModel: AppViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        return
    }

    val navBackStack: MutableList<VibePlayerGraph> =
        rememberSerializable(serializer = SnapshotStateListSerializer()) {
            mutableStateListOf(
                if (state.isAudioPermissionGranted) {
                    VibePlayerGraph.MainScreen
                } else {
                    VibePlayerGraph.PermissionScreen
                }
            )
        }

    NavDisplay(
        backStack = navBackStack,
        onBack = {
            navBackStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<VibePlayerGraph.PermissionScreen> {
                PermissionRoot(
                    onNavigateToMain = {
                        navBackStack.clear()
                        navBackStack.add(VibePlayerGraph.MainScreen)
                    }
                )
            }

            entry<VibePlayerGraph.MainScreen> {
                Text(
                    text = "Main"
                )
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator()
        )
    )
}