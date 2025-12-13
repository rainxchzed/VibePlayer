package zed.rainxch.vibeplayer.feature.main.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import zed.rainxch.vibeplayer.core.presentation.components.buttons.PrimaryButton
import zed.rainxch.vibeplayer.core.presentation.components.progressbars.ScanningProgressbar
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.feature.main.presentation.components.MusicItem

@Composable
fun MainRoot(
    viewModel: MainViewModel = koinViewModel(),
    onNavigateToNowPlaying: (musicId: Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MainScreen(
        state = state,
        onAction = { action ->
            if (action is MainAction.OnMusicItemClick) {
                val music = action.music
                onNavigateToNowPlaying(music.id)
            } else {
                viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun MainScreen(
    state: MainState,
    onAction: (MainAction) -> Unit,
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
                    MainContent(state, onAction)
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
private fun NoMusicContent(onAction: (MainAction) -> Unit) {
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
                onAction(MainAction.OnScanAgainClick)
            }
        )
    }
}

@Composable
private fun MainContent(
    state: MainState,
    onAction: (MainAction) -> Unit
) {
    val listState = rememberLazyListState()
    val shouldShowScrollTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 3
        }
    }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = state.musics,
                key = { it.musicUrl }
            ) { music ->
                MusicItem(
                    music = music,
                    onClick = {
                        onAction(MainAction.OnMusicItemClick(music))
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
                .padding(end = 8.dp, bottom = 12.dp)
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
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
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        MainScreen(
            state = MainState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun Preview2() {
    VibePlayerTheme {
        MainScreen(
            state = MainState(scanResultState = ScanResultState.Ready),
            onAction = {}
        )
    }
}