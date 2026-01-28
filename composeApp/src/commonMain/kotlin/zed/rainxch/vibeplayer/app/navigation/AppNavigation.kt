package zed.rainxch.vibeplayer.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation3.ui.LocalNavAnimatedContentScope
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
import zed.rainxch.vibeplayer.core.presentation.utils.showSnackBar
import zed.rainxch.vibeplayer.feature.main_controller.presentation.MainControllerRoot
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackViewModel
import zed.rainxch.vibeplayer.feature.now_playing.presentation.NowPlayingRoot
import zed.rainxch.vibeplayer.feature.permission.presentation.PermissionRoot
import zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation.AddSongsRoot
import zed.rainxch.vibeplayer.feature.playlistPlayback.PlaylistPlaybackRoot
import zed.rainxch.vibeplayer.feature.scan.presentation.ScanRoot
import zed.rainxch.vibeplayer.feature.search.presentation.SearchRoot
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsViewModel

@Composable
fun AppNavigation(
    viewModel: AppViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val musicPlaybackViewModel: MusicPlaybackViewModel =
        koinViewModel()// Track whether the player is expanded or minimized
    val songsViewModel: SongsViewModel = koinViewModel()

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
                    VibePlayerGraph.MainControllerScreen
                } else {
                    VibePlayerGraph.PermissionScreen
                }
            )
        }


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackBarHostState,
                    modifier = Modifier.imePadding()
                )
            },
            topBar = {
                when (navBackStack.lastOrNull()) {
                    VibePlayerGraph.MainControllerScreen -> {
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

//                    is VibePlayerGraph.NowPlayingScreen -> {
//                        NowPlayingTopbar(
//                            onMinimizeClick = {
//
//                                navBackStack.removeLastOrNull()
//                            }
//                        )
//                    }

                    else -> {}
                }
            },
            containerColor = MaterialTheme.colorScheme.onSecondary,
            contentWindowInsets = WindowInsets(0.dp)
        ) { innerPadding ->
            SharedTransitionLayout {
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
                                    navBackStack.add(VibePlayerGraph.MainControllerScreen)
                                }
                            )
                        }

                        entry<VibePlayerGraph.MainControllerScreen> {
                            MainControllerRoot(
                                onShowSnackbar = { message ->
                                    coroutineScope.launch {
                                        snackBarHostState.currentSnackbarData?.dismiss()
                                        snackBarHostState.showSnackbar(
                                            message = message,
                                        )
                                    }
                                },
                                onNavigateToNowPlaying = { musicId ->
                                    navBackStack.add(VibePlayerGraph.NowPlayingScreen(musicId))
                                },
                                onExpandPlayer = {
                                    navBackStack.add(VibePlayerGraph.NowPlayingScreen())
                                },
                                onNavigateToAddSongs = { playListId ->
                                    navBackStack.add(VibePlayerGraph.AddSongsScreen(playListId = playListId))
                                },
                                onNavigateToPlaylist = {
                                    navBackStack.add(
                                        VibePlayerGraph.PlaylistPlaybackScreen(
                                            it,
                                            false
                                        )
                                    )
                                },
                                onNavigateToPlaylistPlayback = { playlistId, startPlaylistPlayback ->
                                    navBackStack.add(
                                        VibePlayerGraph.PlaylistPlaybackScreen(
                                            playlistId,
                                            startPlaylistPlayback
                                        )
                                    )
                                },
                                musicPlaybackViewModel = musicPlaybackViewModel
                            )
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
                                    navBackStack.add(VibePlayerGraph.NowPlayingScreen(it.id))
                                }
                            )
                        }

                        entry<VibePlayerGraph.AddSongsScreen> { route ->
                            AddSongsRoot(
                                songsViewModel = songsViewModel, onBackPressed = {
                                    navBackStack.removeLast()
                                },
                                playlistId = route.playListId,
                                onShowSnackBar = { message ->
                                    coroutineScope.showSnackBar(
                                        snackBarHostState = snackBarHostState,
                                        message = message
                                    )
                                })
                        }

                        entry<VibePlayerGraph.PlaylistPlaybackScreen> { route ->
                            PlaylistPlaybackRoot(
                                navigateBack = {
                                    navBackStack.removeLastOrNull()
                                },
                                id = route.playListId,
                                navigateToAddSongs = {
                                    navBackStack.add(VibePlayerGraph.AddSongsScreen(playListId = route.playListId))
                                },
                                onNavigateToNowPlaying = {
                                    navBackStack.add(VibePlayerGraph.NowPlayingScreen())
                                },
                            )
                        }

                        entry<VibePlayerGraph.NowPlayingScreen>(
                            metadata = NavDisplay.transitionSpec {
                                // Slide new content up, keeping the old content in place underneath
                                slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = tween(300)
                                ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                            } + NavDisplay.popTransitionSpec {
                                // Slide old content down, revealing the new content in place underneath
                                EnterTransition.None togetherWith
                                        slideOutVertically(
                                            targetOffsetY = { it },
                                            animationSpec = tween(300)
                                        )
                            } + NavDisplay.predictivePopTransitionSpec {
                                // Slide old content down, revealing the new content in place underneath
                                EnterTransition.None togetherWith
                                        slideOutVertically(
                                            targetOffsetY = { it },
                                            animationSpec = tween(300)
                                        )
                            }
                        ) { route ->
                            LaunchedEffect(route.id) {
                                route.id?.let {
                                    val selectedMusic = songsViewModel.getMusicById(it)
                                    musicPlaybackViewModel.loadSelectedMusic(selectedMusic)
                                }
                            }

                            NowPlayingRoot(
                                musicPlaybackViewModel = musicPlaybackViewModel,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = LocalNavAnimatedContentScope.current,
                                onNavigateBack = {
                                    navBackStack.removeLastOrNull()
                                },
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

//        AnimatedVisibility(
//            visible = isPlayerExpanded,
//            enter = slideInVertically(
//                initialOffsetY = { it }, // Starts from bottom
//                animationSpec = tween(
//                    durationMillis = 400,
//                    easing = FastOutSlowInEasing
//                )
//            ),
//            exit = slideOutVertically(
//                targetOffsetY = { it }, // Slides to bottom
//                animationSpec = tween(
//                    durationMillis = 400,
//                    easing = FastOutSlowInEasing
//                )
//            )
//        ) {
//            currentMusicId?.let { id ->
//                NowPlayingRoot(
//                    musicId = id,
//                    musicPlaybackViewModel = musicPlaybackViewModel,
//                    onMinimize = { isPlayerExpanded = false }
//                )
//            }
//        }
    }
}
