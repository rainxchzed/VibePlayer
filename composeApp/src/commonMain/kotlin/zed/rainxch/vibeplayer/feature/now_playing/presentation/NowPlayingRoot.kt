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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.cd_minimize
import vibeplayer.composeapp.generated.resources.cd_next_track_button
import vibeplayer.composeapp.generated.resources.cd_play_pause_button
import vibeplayer.composeapp.generated.resources.cd_previous_track_button
import vibeplayer.composeapp.generated.resources.cd_repeat_button
import vibeplayer.composeapp.generated.resources.cd_shuffle_button
import vibeplayer.composeapp.generated.resources.ic_playlist
import vibeplayer.composeapp.generated.resources.pause
import vibeplayer.composeapp.generated.resources.play
import vibeplayer.composeapp.generated.resources.repeat_all
import vibeplayer.composeapp.generated.resources.repeat_none
import vibeplayer.composeapp.generated.resources.repeat_one
import vibeplayer.composeapp.generated.resources.shuffle
import vibeplayer.composeapp.generated.resources.skip_next
import vibeplayer.composeapp.generated.resources.skip_previous
import zed.rainxch.vibeplayer.core.presentation.components.CreateNewPlaylistBottomSheet
import zed.rainxch.vibeplayer.core.presentation.utils.ObserveAsEvents
import zed.rainxch.vibeplayer.feature.now_playing.presentation.components.MusicContentItem
import zed.rainxch.vibeplayer.feature.now_playing.presentation.components.PlayerControlSlider
import zed.rainxch.vibeplayer.feature.now_playing.presentation.components.SelectPlaylistBottomSheet
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsAction
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingRoot(
    viewModel: SongsViewModel = koinViewModel(),
    musicPlaybackViewModel: MusicPlaybackViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onNavigateBack: () -> Unit,
) {

    val musicPlaybackState by musicPlaybackViewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    ObserveAsEvents(musicPlaybackViewModel.events) { event ->
        when (event) {
            is NowPlayingEvent.OnMessage -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    NowPlayingScreen(
        state = musicPlaybackState,
        onAction = { action ->
            when (action) {
                MusicPlaybackAction.OnMinimizeClick -> {
                    viewModel.onAction(SongsAction.OnMinimizeNowPlaying)
                    onNavigateBack()
                }

                else -> {
                    musicPlaybackViewModel.onAction(action)
                }
            }
        },
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        snackbarHostState = snackbarHostState
    )

    if (musicPlaybackState.isSelectPlaylistBottomSheetVisible) {
        SelectPlaylistBottomSheet(
            onDismissRequest = {
                musicPlaybackViewModel.onAction(MusicPlaybackAction.OnCloseAddToPlaylistDialog)
            },
            playlists = musicPlaybackState.playlists,
            onCreatePlaylistClick = {
                musicPlaybackViewModel.onAction(MusicPlaybackAction.OnCreatePlaylistClick)
            },
            onFavouritePlaylistClick = {
                musicPlaybackState.selectedMusic?.let { music ->
                    musicPlaybackViewModel.onAction(
                        MusicPlaybackAction.OnToggleFavouriteMusic(
                            music = music,
                            isFromPlaylistBottomSheet = true
                        )
                    )
                }
            },
            onPlaylistSelected = { playlist ->
                musicPlaybackViewModel.onAction(MusicPlaybackAction.OnPlaylistSelected(playlist))
            }
        )
    }

    if (musicPlaybackState.isCreateNewPlaylistBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                musicPlaybackViewModel.onAction(MusicPlaybackAction.OnCloseCreatePlaylistDialog)
            },
            contentColor = MaterialTheme.colorScheme.onSecondary,
            sheetMaxWidth = 480.dp,
            dragHandle = null,
        ) {
            CreateNewPlaylistBottomSheet(
                playlistName = musicPlaybackState.newPlaylistName,
                onPlaylistNameChange = { name ->
                    musicPlaybackViewModel.onAction(MusicPlaybackAction.OnChangeNewPlaylistName(name))
                },
                onCancel = {
                    musicPlaybackViewModel.onAction(MusicPlaybackAction.OnCloseCreatePlaylistDialog)
                },
                onCreate = {
                    musicPlaybackViewModel.onAction(MusicPlaybackAction.OnCreateNewPlaylistClick)
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.onSecondary)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    state: MusicPlaybackState,
    onAction: (MusicPlaybackAction) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    snackbarHostState: SnackbarHostState,
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


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondary)
                .windowInsetsPadding(WindowInsets.safeDrawing) // Handle system bars
        ) {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(MusicPlaybackAction.OnMinimizeClick)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryFixed,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(Res.string.cd_minimize),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = {
                                onAction(MusicPlaybackAction.OnAddToPlaylistClick)
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryFixed,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_playlist),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        state.selectedMusic?.let { music ->
                            IconButton(
                                onClick = {
                                    onAction(MusicPlaybackAction.OnToggleFavouriteMusic(music))
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryFixed,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (state.isFavourite) {
                                        Icons.Default.Favorite
                                    } else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary
                ),
                title = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 20.dp, end = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    PlayerControlSlider(
                        state = state,
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = sharedTransitionScope.rememberSharedContentState(
                                    key = "progress-bar-${state.selectedMusic?.id}"
                                ),
                                animatedVisibilityScope = animatedContentScope
                            )
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(bottom = 8.dp),
                        onSeek = { positionMs ->
                            onAction(MusicPlaybackAction.OnSeek(positionMs))
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 16.dp,
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 16.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            IconButton(
                                onClick = {
                                    onAction(MusicPlaybackAction.OnShuffleClick)
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = shuffleContainerColor,
                                    contentColor = shuffleContentColor
                                ),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.shuffle),
                                    contentDescription = stringResource(Res.string.cd_shuffle_button),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 10.dp,
                                alignment = Alignment.CenterHorizontally
                            )
                        ) {
                            IconButton(
                                onClick = {
                                    onAction(MusicPlaybackAction.OnPreviousClick)
                                },
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
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                modifier = Modifier
                                    .sharedElement(
                                        sharedContentState = sharedTransitionScope.rememberSharedContentState(
                                            key = "play-pause-button-${state.selectedMusic?.id}"
                                        ),
                                        animatedVisibilityScope = animatedContentScope,
                                    )
                                    .size(60.dp)
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = if (state.isPlaying) {
                                        painterResource(Res.drawable.pause)
                                    } else painterResource(Res.drawable.play),
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
                        }

                        Box(
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(
                                onClick = {
                                    onAction(MusicPlaybackAction.OnRepeatClick)
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = repeatContainerColor,
                                    contentColor = repeatContentColor
                                ),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(
                                    painter = repeatIcon,
                                    contentDescription = stringResource(Res.string.cd_repeat_button),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
