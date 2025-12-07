package zed.rainxch.vibeplayer.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_scan
import zed.rainxch.vibeplayer.AppViewModel
import zed.rainxch.vibeplayer.core.presentation.components.topbars.MainTopbar
import zed.rainxch.vibeplayer.core.presentation.components.topbars.ScanTopbar
import zed.rainxch.vibeplayer.feature.main.presentation.MainRoot
import zed.rainxch.vibeplayer.feature.permission.presentation.PermissionRoot
import zed.rainxch.vibeplayer.feature.scan.presentation.ScanRoot

@Composable
fun AppNavigation(
    viewModel: AppViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    if (state.isLoading) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondary)
        )

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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            when (navBackStack.lastOrNull()) {
                VibePlayerGraph.MainScreen -> {
                    MainTopbar(
                        actions = {
                            IconButton(
                                onClick = {
                                    navBackStack.add(VibePlayerGraph.ScanScreen)
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryFixed,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_scan),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    )
                }
                VibePlayerGraph.ScanScreen -> {
                    ScanTopbar(
                        onBackPressed = {
                            navBackStack.removeLastOrNull()
                        }
                    )
                }


                VibePlayerGraph.PermissionScreen, null -> {}
            }
        },
        containerColor = MaterialTheme.colorScheme.onSecondary
    ) { innerPadding ->
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
                    MainRoot()
                }

                entry<VibePlayerGraph.ScanScreen> {
                    ScanRoot(
                        snackbarHostState = snackbarHostState,
                    )
                }
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator()
            ),
            modifier = Modifier.padding(innerPadding)
        )
    }
}