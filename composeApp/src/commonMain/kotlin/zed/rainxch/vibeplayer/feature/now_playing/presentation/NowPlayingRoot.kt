package zed.rainxch.vibeplayer.feature.now_playing.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.pause
import vibeplayer.composeapp.generated.resources.play
import vibeplayer.composeapp.generated.resources.skip_next
import vibeplayer.composeapp.generated.resources.skip_previous
import zed.rainxch.vibeplayer.feature.main.presentation.MainViewModel
import zed.rainxch.vibeplayer.feature.now_playing.presentation.components.MusicContentItem

@Composable
fun NowPlayingRoot(
    musicId: Int,
    viewModel: MainViewModel = koinViewModel(),
    musicPlaybackViewModel: MusicPlaybackViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val musicPlaybackState by musicPlaybackViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(musicId) {
        val selectedMusic = viewModel.getMusicById(musicId)
        musicPlaybackViewModel.loadSelectedMusic(selectedMusic)
    }

    NowPlayingScreen(state = musicPlaybackState, onAction = { action ->
        musicPlaybackViewModel.onAction(action)
    })
}

@Composable
fun NowPlayingScreen(
    state: MusicPlaybackState,
    onAction: (MusicPlaybackAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)

    ) {
        if (state.selectedMusic != null)
            MusicContentItem(state.selectedMusic, modifier = Modifier.align(Alignment.Center))

        Column(
            modifier = Modifier.fillMaxWidth(0.9f).wrapContentHeight().padding(bottom = 16.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { 0.57f },
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
                trackColor = MaterialTheme.colorScheme.outline,
                drawStopIndicator = {})


            Row(
                modifier = Modifier.wrapContentSize().padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = {},
                    shape = CircleShape,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryFixed,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.skip_previous),
                        contentDescription = "previous"
                    )
                }

                IconButton(
                    onClick = {
                        if (state.isPlaying) onAction(MusicPlaybackAction.onPauseClick)
                        else onAction(
                            MusicPlaybackAction.onPlayClick
                        )
                    }, shape = CircleShape, colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,

                        ), modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        painter = if (state.isPlaying) painterResource(Res.drawable.pause) else painterResource(
                            Res.drawable.play
                        ),
                        contentDescription = "play/pause"
                    )
                }

                IconButton(
                    onClick = {}, shape = CircleShape,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryFixed,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.skip_next),
                        contentDescription = "next"
                    )
                }
            }
        }
    }
}
