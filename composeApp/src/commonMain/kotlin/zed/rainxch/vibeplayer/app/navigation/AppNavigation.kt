package zed.rainxch.vibeplayer.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_scan
import zed.rainxch.vibeplayer.AppViewModel
import zed.rainxch.vibeplayer.core.presentation.components.topbars.MainTopbar
import zed.rainxch.vibeplayer.core.presentation.components.topbars.ScanTopbar
import zed.rainxch.vibeplayer.feature.main.presentation.MainRoot
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackViewModel
import zed.rainxch.vibeplayer.feature.now_playing.presentation.NowPlayingRoot
import zed.rainxch.vibeplayer.feature.permission.presentation.PermissionRoot
import zed.rainxch.vibeplayer.feature.scan.presentation.ScanRoot
import zed.rainxch.vibeplayer.feature.search.presentation.SearchRoot

@Composable
fun AppNavigation(
    viewModel: AppViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val musicPlaybackViewModel: MusicPlaybackViewModel = koinViewModel()// Track whether the player is expanded or minimized
    var currentMusicId by remember { mutableStateOf<Int?>(null) }
    var isPlayerExpanded by remember { mutableStateOf(false) }

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


Box(modifier = Modifier.fillMaxSize()){
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            when (navBackStack.lastOrNull()) {
                VibePlayerGraph.MainScreen -> {
                    MainTopbar(
                        actions = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
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

                                IconButton(
                                    onClick = {
                                        navBackStack.add(VibePlayerGraph.SearchScreen)
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryFixed,
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
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

                VibePlayerGraph.PermissionScreen, VibePlayerGraph.SearchScreen, null -> {}
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
                    MainRoot(onNavigateToNowPlaying = { musicId ->
                        currentMusicId = musicId
                        isPlayerExpanded = true })
                }

                entry<VibePlayerGraph.ScanScreen> {
                    ScanRoot(
                        onShowSnackbar = { message ->
                            coroutineScope.launch {
                                snackBarHostState.currentSnackbarData?.dismiss()
                                snackBarHostState.showSnackbar(
                                    message = message,
                                )
                            }
                        },
                        navigateBack = {
                            navBackStack.removeLastOrNull()
                        }
                    )
                }

                entry<VibePlayerGraph.SearchScreen> {
                    SearchRoot(
                        onBackClick = {
                            navBackStack.removeLastOrNull()
                        },
                        onNavigateToNowPlayingScreen = {
                            currentMusicId = it.id
                            isPlayerExpanded = true
                        }
                    )
                }
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator()
            ),
            modifier = Modifier.padding(innerPadding)
        )
    }

    AnimatedVisibility(
        visible = isPlayerExpanded,
        enter = slideInVertically(
            initialOffsetY = { it }, // Starts from bottom
            animationSpec = tween(durationMillis = 500)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }, // Slides to bottom
            animationSpec = tween(durationMillis = 500)
        )
    ) {
        currentMusicId?.let { id ->
            NowPlayingRoot(
                musicId = id,
                musicPlaybackViewModel = musicPlaybackViewModel,
                onMinimize = { isPlayerExpanded = false }
            )
        }
    }
}
}