package zed.rainxch.vibeplayer.feature.songs.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.songs_screen_songs_count
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.presentation.components.MusicItem
import zed.rainxch.vibeplayer.core.presentation.components.buttons.PrimaryButton
import zed.rainxch.vibeplayer.core.presentation.components.progressbars.ScanningProgressbar
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.core.presentation.components.QuickPlayBar
import zed.rainxch.vibeplayer.feature.mini_player.MiniPlayer
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackAction
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackViewModel

@Composable
fun SongsRoot(
    viewModel: SongsViewModel = koinViewModel(),
    musicPlaybackViewModel: MusicPlaybackViewModel = koinViewModel(),
    onNavigateToNowPlaying: (musicId: Int) -> Unit,
    onExpandPlayer: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SongsScreen(
        state = state,
        onPlayAllClick = {
            musicPlaybackViewModel.createPlayList(state.musics)
            musicPlaybackViewModel.onAction(MusicPlaybackAction.OnPlayAllClick)
            onExpandPlayer()
        },
        onShuffleAndPlayClick = {
            musicPlaybackViewModel.createPlayList(state.musics)
            musicPlaybackViewModel.onAction(MusicPlaybackAction.OnShuffleAndPlayClick)
            onExpandPlayer()
        },
        onAction = { action ->
            if (action is SongsAction.OnMusicItemClick) {
                val music = action.music
                onNavigateToNowPlaying(music.id)
            } else {
                viewModel.onAction(action)
            }
        },
        onExpandPlayer = onExpandPlayer,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope
    )
}

@Composable
fun SongsScreen(
    state: SongsState,
    onPlayAllClick: () -> Unit,
    onShuffleAndPlayClick: () -> Unit,
    onAction: (SongsAction) -> Unit,
    onExpandPlayer: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
    ) {
        when (state.scanResultState) {
            ScanResultState.Loading -> {
                LoadingContainer()
            }

            ScanResultState.Ready -> {
                if (state.musics.isEmpty()) {
                    NoMusicContent(onAction)
                } else {
                    SongsMainContent(
                        state = state,
                        onPlayAllClick = onPlayAllClick,
                        onShuffleAndPlayClick = onShuffleAndPlayClick,
                        onAction = onAction,
                        onExpandPlayer = onExpandPlayer,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContainer() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScanningProgressbar()

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Scanning your device for music...",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun NoMusicContent(onAction: (SongsAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No music found",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Try scanning again or check your folders.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            text = "Scan again",
            onClick = {
                onAction(SongsAction.OnScanAgainClick)
            }
        )
    }
}

@Composable
private fun SongsMainContent(
    state: SongsState,
    onPlayAllClick: () -> Unit,
    onShuffleAndPlayClick: () -> Unit,
    onAction: (SongsAction) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onExpandPlayer: () -> Unit
) {
    val listState = rememberLazyListState()
    val shouldShowScrollTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 3
        }
    }
    val coroutineScope = rememberCoroutineScope()

    // MiniPlayer height: 64dp (content) + 32dp (padding) = 96dp
    val miniPlayerBottomPadding = if (state.miniPlayerVisible) 96.dp else 0.dp

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = miniPlayerBottomPadding
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                QuickPlayBar(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    onPlayClick = onPlayAllClick,
                    onShuffleClick = onShuffleAndPlayClick
                )
            }
            item {
                Text(
                    text = pluralStringResource(
                        Res.plurals.songs_screen_songs_count,
                        state.musics.size,
                        state.musics.size
                    ),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            items(
                items = state.musics,
                key = { it.musicUrl }
            ) { music ->
                MusicItem(
                    music = music,
                    onClick = {
                        onAction(SongsAction.OnMusicItemClick(music))
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = shouldShowScrollTop,
            enter = slideInVertically(initialOffsetY = {
                it / 2
            }),
            exit = slideOutVertically(targetOffsetY = {
                it
            }),
            modifier = Modifier
                .padding(end = 8.dp, bottom = 12.dp + miniPlayerBottomPadding)
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = null
                )
            }
        }

        AnimatedVisibility(
            visible = state.miniPlayerVisible,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            MiniPlayer(
                onExpand = onExpandPlayer,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        SharedTransitionLayout {
            SongsScreen(
                state = SongsState(),
                onPlayAllClick = {},
                onShuffleAndPlayClick = {},
                onAction = {},
                onExpandPlayer = {},
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedContentScope = LocalNavAnimatedContentScope.current
            )
        }
    }
}

@Preview
@Composable
private fun Preview2() {
    VibePlayerTheme {
        SharedTransitionLayout {
            SongsScreen(
                state = SongsState(scanResultState = ScanResultState.Ready),
                onPlayAllClick = {},
                onShuffleAndPlayClick = {},
                onAction = {},
                onExpandPlayer = {},
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedContentScope = LocalNavAnimatedContentScope.current
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMiniPlayer() {
    VibePlayerTheme {
        SharedTransitionLayout {
            SongsScreen(
                state = SongsState(
                    scanResultState = ScanResultState.Ready,
                    musics = persistentListOf(
                        Music(
                            title = "505",
                            duration = "4:14",
                            artist = "Arctic Monkeys",
                            musicUrl = "music",
                            isFavourite = false
                        )
                    )
                ),
                onPlayAllClick = {},
                onShuffleAndPlayClick = {},
                onAction = {},
                onExpandPlayer = {},
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedContentScope = LocalNavAnimatedContentScope.current
            )
        }
    }
}
