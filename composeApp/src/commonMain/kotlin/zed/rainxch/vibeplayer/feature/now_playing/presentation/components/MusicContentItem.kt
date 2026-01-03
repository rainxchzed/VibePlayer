package zed.rainxch.vibeplayer.feature.now_playing.presentation.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_music
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun MusicContentItem(
    music: Music,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    with (sharedTransitionScope) {

        Column(
            modifier = modifier.fillMaxWidth(0.9f).wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {

            if (music.bannerUrl == null) {
                Image(
                    painter = painterResource(Res.drawable.ic_music),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = "banner-placeholder-${music.id}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                        .size(240.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.secondary.copy(alpha = .2f),
                                )
                            )
                        )
                        .padding(12.dp)
                    ,
                )

            } else {
                AsyncImage(
                    model = music.bannerUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = "banner-image-${music.id}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth().wrapContentHeight().padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "title-${music.id}"),
                        animatedVisibilityScope = animatedContentScope
                    ),
                    text = music.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    modifier = Modifier.sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "artist-${music.id}"),
                        animatedVisibilityScope = animatedContentScope
                    ),
                    text = music.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicItemPreview() {
    VibePlayerTheme {
        SharedTransitionLayout {
            MusicContentItem(
                music = Music(
                    title = "505",
                    duration = "4:14",
                    artist = "Arctic Monkeys",
                    bannerUrl = null,
                    musicUrl = "",
                    isFavourite = false
                ),
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedContentScope = LocalNavAnimatedContentScope.current
            )
        }

    }
}
