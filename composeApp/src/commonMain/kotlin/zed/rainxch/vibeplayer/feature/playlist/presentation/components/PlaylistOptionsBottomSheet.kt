package zed.rainxch.vibeplayer.feature.playlist.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.change_cover
import vibeplayer.composeapp.generated.resources.delete
import vibeplayer.composeapp.generated.resources.ic_playlist
import vibeplayer.composeapp.generated.resources.play
import vibeplayer.composeapp.generated.resources.playlist_change_cover
import vibeplayer.composeapp.generated.resources.playlist_delete
import vibeplayer.composeapp.generated.resources.playlist_play
import vibeplayer.composeapp.generated.resources.playlist_rename
import vibeplayer.composeapp.generated.resources.rename
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.feature.playlist.presentation.PlaylistAction

@Composable
fun PlayListOptionsBottomSheet(
    id: Int,
    title: String,
    songsCount: Int,
    coverImage: String?,
    onAction: (PlaylistAction) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            )
            .imePadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PlaylistSheetItem(
            title = title,
            songsCount = songsCount,
            icon = {
                if (coverImage == null) {
                    Image(
                        painter = painterResource(Res.drawable.ic_playlist),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xffDE84FF),
                                        Color(0xffDE84FF).copy(alpha = .2f),
                                    )
                                )
                            )
                            .padding(14.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = coverImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onError = {
                                println("Coil: Error loading image: ${it.result.throwable}")
                            }
                        )
                    }
                }
            },
        )


        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline
        )

        PlaylistActionItem(
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.playlist_play),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified
                )
            },
            actionName = stringResource(Res.string.play),
            onClick = { onAction(PlaylistAction.OnPlayPlaylistClick(id)) },
            modifier = Modifier.fillMaxWidth()
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.onSecondary)

        )

        PlaylistActionItem(
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.playlist_rename),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified
                )
            },
            actionName = stringResource(Res.string.rename),
            onClick = { onAction(PlaylistAction.OnRenamePlaylistClick(id, title)) },
            modifier = Modifier.fillMaxWidth()
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.onSecondary)

        )

        PlaylistActionItem(
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.playlist_change_cover),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified
                )
            },
            actionName = stringResource(Res.string.change_cover),
            onClick = { onAction(PlaylistAction.OnChangeCoverClick(id)) },
            modifier = Modifier.fillMaxWidth()
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.onSecondary)

        )

        PlaylistActionItem(
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.playlist_delete),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Unspecified
                )
            },
            actionName = stringResource(Res.string.delete),
            onClick = { onAction(PlaylistAction.OnDeletePlaylistClick(id, title)) },
            modifier = Modifier.fillMaxWidth()
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
        )

    }
}

@Composable
@Preview
fun PlaylistOptionsBottomSheetPreview() {
    VibePlayerTheme {
        PlayListOptionsBottomSheet(
            id = 1,
            title = "Test",
            songsCount = 5,
            coverImage = "", onAction = {},
            modifier = Modifier
        )

    }
}