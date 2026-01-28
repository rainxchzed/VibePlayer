package zed.rainxch.vibeplayer.feature.playlistPlayback

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_arrow_left
import vibeplayer.composeapp.generated.resources.ic_plus
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.presentation.components.MusicItem
import zed.rainxch.vibeplayer.core.presentation.components.QuickPlayBar
import zed.rainxch.vibeplayer.core.presentation.components.buttons.AppOutlinedButton
import zed.rainxch.vibeplayer.core.presentation.theme.HostGroteskFontFamily
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackAction
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackViewModel
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.SongsCountHeader

@Composable
fun PlaylistPlaybackRoot(
    id: Int,
    startPlaying: Boolean = false,
    navigateBack: () -> Unit,
    navigateToAddSongs: () -> Unit,
    musicPlaybackViewModel: MusicPlaybackViewModel = koinViewModel(),
    onNavigateToNowPlaying: (id: Int?) -> Unit,
    viewModel: PlaylistPlaybackViewModel = koinViewModel(
        key = id.toString(),
    ) { parametersOf(id) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val playPlaylist: (Boolean) -> Unit = { isShuffled ->
        musicPlaybackViewModel.createPlayList(state.songs)
        if (isShuffled) {
            musicPlaybackViewModel.onAction(MusicPlaybackAction.OnShuffleAndPlayClick)
        } else {
            musicPlaybackViewModel.onAction(MusicPlaybackAction.OnPlayAllClick)
        }
        onNavigateToNowPlaying(null)
    }

    LaunchedEffect(startPlaying, state.songs.isNotEmpty()) {
        if (startPlaying && state.songs.isNotEmpty()) {
            playPlaylist(false)
        }
    }

    PlaylistPlaybackScreen(
        state = state,
        onAction = {
            when (it) {
                PlaylistPlaybackAction.NavigateBack -> navigateBack()
                PlaylistPlaybackAction.AddSongs -> navigateToAddSongs()
                PlaylistPlaybackAction.Play -> {
                    playPlaylist(false)
                }

                PlaylistPlaybackAction.Shuffle -> {
                    playPlaylist(true)
                }

                is PlaylistPlaybackAction.PlayMusicWithId -> {
                    onNavigateToNowPlaying(it.musicId)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistPlaybackScreen(
    state: PlaylistPlaybackState,
    onAction: (PlaylistPlaybackAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(PlaylistPlaybackAction.NavigateBack)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryFixed,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_left),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        },
        containerColor = MaterialTheme.colorScheme.onSecondary
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                if (state.image == null) {
                    Box(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .size(200.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primary.copy(alpha = .2f)
                                    )
                                ),
                                CircleShape,
                                alpha = .4f
                            )
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(120.dp),
                            painter = painterResource(state.defaultImage),
                            contentDescription = null,
                        )
                    }

                } else {
                    AsyncImage(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .size(200.dp)
                            .clip(CircleShape),
                        model = state.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }


                Text(
                    text = state.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(top = 28.dp, bottom = 30.dp)
                )
            }

            if (state.songs.isNotEmpty()) {
                item {
                    QuickPlayBar(
                        onPlayClick = {
                            onAction(PlaylistPlaybackAction.Play)
                        },
                        onShuffleClick = {
                            onAction(PlaylistPlaybackAction.Shuffle)
                        }
                    )

                    SongsCountHeader(
                        totalCount = state.songs.size,
                        onAddClick = {
                            onAction(PlaylistPlaybackAction.AddSongs)
                        }
                    )
                }

                items(state.songs) { music ->
                    MusicItem(
                        music = music,
                        onClick = {
                            onAction(PlaylistPlaybackAction.PlayMusicWithId(music.id))
                        }
                    )
                }

            } else {
                item {
                    Text(
                        text = "No songs found",
                        modifier = Modifier.padding(top = 16.dp),
                        style = TextStyle(
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            fontFamily = HostGroteskFontFamily,
                            fontWeight = FontWeight.Normal,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    AppOutlinedButton(
                        modifier = Modifier.padding(top = 8.dp),
                        onClick = {
                            onAction(PlaylistPlaybackAction.AddSongs)
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                4.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_plus),
                                contentDescription = "Add playlist",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                modifier = Modifier,
                                text = "Add Songs",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        PlaylistPlaybackScreen(
            state = PlaylistPlaybackState(
                title = "Playlist title",
                songs = listOf(
                    Music(
                        id = 1,
                        title = "Song title",
                        artist = "Artist name",
                        duration = "123",
                        bannerUrl = "",
                        musicUrl = "",
                        isFavourite = true,
                    )
                )
            ),
            onAction = {}
        )
    }
}
