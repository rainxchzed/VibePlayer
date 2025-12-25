package zed.rainxch.vibeplayer.feature.mini_player

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.cd_next_track_button
import vibeplayer.composeapp.generated.resources.cd_play_pause_button
import vibeplayer.composeapp.generated.resources.ic_music
import vibeplayer.composeapp.generated.resources.pause
import vibeplayer.composeapp.generated.resources.play
import vibeplayer.composeapp.generated.resources.skip_next
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackAction
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackState
import zed.rainxch.vibeplayer.feature.now_playing.presentation.MusicPlaybackViewModel

@Composable
fun MiniPlayer(
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: MusicPlaybackViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.selectedMusic == null) return

    val progress = if (state.duration > 0) {
        state.currentProgress.toFloat() / state.duration.toFloat()
    } else {
        0f
    }

    MiniPlayerContent(
        state = state,
        onAction = viewModel::onAction,
        progress = progress,
        modifier = modifier,
        onExpand = onExpand,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope
    )
}

@Composable
private fun MiniPlayerContent(
    state: MusicPlaybackState,
    onAction: (MusicPlaybackAction) -> Unit,
    progress: Float,
    modifier: Modifier = Modifier,
    onExpand: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.outline)
            .clickable { onExpand() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.selectedMusic?.let {
                with(sharedTransitionScope) {
                    // Album Artwork
                    if (it.bannerUrl == null) {
                        Image(
                            painter = painterResource(Res.drawable.ic_music),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(key = "banner-placeholder-${it.id}"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.secondary,
                                            MaterialTheme.colorScheme.secondary.copy(alpha = .2f),
                                        )
                                    )
                                )
                                .padding(12.dp),
                        )
                    } else {
                        AsyncImage(
                            model = it.bannerUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(key = "banner-image-${it.id}"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    modifier = Modifier.sharedElement(
                                        sharedTransitionScope.rememberSharedContentState(key = "title-${it.id}"),
                                        animatedVisibilityScope = animatedContentScope
                                    ),
                                    text = it.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )

                                Text(
                                    modifier = Modifier.sharedElement(
                                        sharedTransitionScope.rememberSharedContentState(key = "artist-${it.id}"),
                                        animatedVisibilityScope = animatedContentScope
                                    ),
                                    text = it.artist,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }

                            // Play/Pause Button
                            IconButton(
                                onClick = {
                                    if (state.isPlaying) {
                                        onAction(MusicPlaybackAction.OnPauseClick)
                                    } else {
                                        onAction(MusicPlaybackAction.OnPlayClick)
                                    }
                                },
                                shape = CircleShape,
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface,
                                    contentColor = MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .sharedElement(
                                        sharedTransitionScope.rememberSharedContentState(key = "play-pause-button-${it.id}"),
                                        animatedContentScope,
                                    )
                                    .size(44.dp)
                            ) {
                                Icon(
                                    painter = if (state.isPlaying) {
                                        painterResource(Res.drawable.pause)
                                    } else {
                                        painterResource(Res.drawable.play)
                                    },
                                    contentDescription = stringResource(Res.string.cd_play_pause_button),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Next Button
                            IconButton(
                                onClick = { onAction(MusicPlaybackAction.OnNextClick) },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                    .sharedElement(
                                        sharedContentState = sharedTransitionScope.rememberSharedContentState(
                                            key = "next-button-${it.id}"
                                        ),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                    .size(44.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.skip_next),
                                    contentDescription = stringResource(Res.string.cd_next_track_button),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        // Progress Bar
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(key = "progress-bar-${it.id}"),
                                    animatedContentScope
                                )
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurface,
                            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .15f),
                            strokeCap = StrokeCap.Round,
                            drawStopIndicator = {},
                            gapSize = 0.dp
                        )
                    }


                }

            }

        }


    }
}

@Composable
@Preview
private fun Preview() {
    VibePlayerTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) { // visible is true to show the final state
                MiniPlayerContent(
                    state = MusicPlaybackState(
                        selectedMusic = Music(
                            id = 1,
                            title = "Title",
                            artist = "Artist",
                            bannerUrl = null,
                            duration = "3:00",
                            musicUrl = ""
                        )
                    ),
                    onAction = {},
                    progress = 0.5f,
                    onExpand = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this
                )
            }
        }
    }
}
