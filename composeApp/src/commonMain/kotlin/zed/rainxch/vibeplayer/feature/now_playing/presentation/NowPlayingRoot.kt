package zed.rainxch.vibeplayer.feature.now_playing.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.cd_next_track_button
import vibeplayer.composeapp.generated.resources.cd_play_pause_button
import vibeplayer.composeapp.generated.resources.cd_previous_track_button
import vibeplayer.composeapp.generated.resources.cd_repeat_button
import vibeplayer.composeapp.generated.resources.cd_shuffle_button
import vibeplayer.composeapp.generated.resources.pause
import vibeplayer.composeapp.generated.resources.play
import vibeplayer.composeapp.generated.resources.repeat_all
import vibeplayer.composeapp.generated.resources.repeat_none
import vibeplayer.composeapp.generated.resources.repeat_one
import vibeplayer.composeapp.generated.resources.shuffle
import vibeplayer.composeapp.generated.resources.skip_next
import vibeplayer.composeapp.generated.resources.skip_previous
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsViewModel
import zed.rainxch.vibeplayer.feature.now_playing.presentation.components.MusicContentItem
import zed.rainxch.vibeplayer.feature.now_playing.presentation.components.PlayerControlSlider

@Composable
fun NowPlayingRoot(
    viewModel: SongsViewModel = koinViewModel(),
    musicPlaybackViewModel: MusicPlaybackViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val musicPlaybackState by musicPlaybackViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        musicPlaybackViewModel.createPlayList(state.musics)
    }

    /*  DisposableEffect(Unit) {
          onDispose {
              musicPlaybackViewModel.stopProgressTracking()
              musicPlaybackViewModel.stopMusic()
          }
      }*/

    NowPlayingScreen(
        state = musicPlaybackState,
        onAction = musicPlaybackViewModel::onAction,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope
    )
}

@Composable
fun NowPlayingScreen(
    state: MusicPlaybackState,
    onAction: (MusicPlaybackAction) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {

    val repeatIcon = when (state.repeatMode) {
        RepeatMode.NONE -> painterResource(Res.drawable.repeat_none)
        RepeatMode.REPEAT_ALL -> painterResource(Res.drawable.repeat_all)
        RepeatMode.REPEAT_ONE -> painterResource(Res.drawable.repeat_one)
    }

    val isRepeatActive = state.repeatMode != RepeatMode.NONE

    val repeatContainerColor = if (isRepeatActive) {
        MaterialTheme.colorScheme.primaryFixed
    } else {
        Color.Transparent
    }

    val repeatContentColor = if (state.repeatMode == RepeatMode.NONE) {
        MaterialTheme.colorScheme.inverseOnSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val shuffleContainerColor =
        if (state.shuffleMode == ShuffleMode.INACTIVE) Color.Transparent else MaterialTheme.colorScheme.primaryFixed
    val shuffleContentColor =
        if (state.shuffleMode == ShuffleMode.INACTIVE) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.onSurfaceVariant


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .windowInsetsPadding(WindowInsets.safeDrawing) // Handle system bars
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.onSecondary),
            contentAlignment = Alignment.Center

        ) {
            if (state.selectedMusic != null)
                MusicContentItem(
                    state.selectedMusic,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                )
        }

        with(sharedTransitionScope) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    .padding(start = 20.dp, end = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                PlayerControlSlider(
                    state = state,
                    modifier = Modifier
                        .sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = "progress-bar-${state.selectedMusic?.id}"),
                            animatedContentScope
                        )
                        .fillMaxWidth().wrapContentHeight().padding(bottom = 8.dp),
                    onSeek = { positionMs ->
                        onAction(MusicPlaybackAction.OnSeek(positionMs))
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        IconButton(
                            onClick = { onAction(MusicPlaybackAction.OnShuffleClick) },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = shuffleContainerColor,
                                contentColor = shuffleContentColor
                            )
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.shuffle),
                                contentDescription = stringResource(Res.string.cd_shuffle_button),
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.weight(3f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = { onAction(MusicPlaybackAction.OnPreviousClick) },
                            shape = CircleShape,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryFixed,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(Res.drawable.skip_previous),
                                contentDescription = stringResource(Res.string.cd_previous_track_button)
                            )
                        }

                        IconButton(
                            onClick = {
                                if (state.isPlaying) onAction(MusicPlaybackAction.OnPauseClick)
                                else onAction(
                                    MusicPlaybackAction.OnPlayClick
                                )
                            },
                            shape = CircleShape,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.onSurface,
                            ),
                            modifier = Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(key = "play-pause-button-${state.selectedMusic?.id}"),
                                    animatedContentScope,
                                )
                                .size(60.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = if (state.isPlaying) painterResource(Res.drawable.pause) else painterResource(
                                    Res.drawable.play
                                ),
                                contentDescription = stringResource(Res.string.cd_play_pause_button)
                            )
                        }

                        IconButton(
                            onClick = { onAction(MusicPlaybackAction.OnNextClick) },
                            shape = CircleShape,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryFixed,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = sharedTransitionScope.rememberSharedContentState(
                                        key = "next-button-${state.selectedMusic?.id}"
                                    ),
                                    animatedVisibilityScope = animatedContentScope
                                )
                                .size(44.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(Res.drawable.skip_next),
                                contentDescription = stringResource(Res.string.cd_next_track_button)
                            )
                        }

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(
                                onClick = { onAction(MusicPlaybackAction.OnRepeatClick) },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = repeatContainerColor,
                                    contentColor = repeatContentColor
                                )
                            ) {
                                Icon(
                                    painter = repeatIcon,
                                    contentDescription = stringResource(Res.string.cd_repeat_button)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
