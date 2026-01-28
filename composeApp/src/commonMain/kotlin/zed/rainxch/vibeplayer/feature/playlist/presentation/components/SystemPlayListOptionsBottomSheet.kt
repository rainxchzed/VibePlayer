package zed.rainxch.vibeplayer.feature.playlist.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_heart
import vibeplayer.composeapp.generated.resources.play
import vibeplayer.composeapp.generated.resources.playlist_play
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.feature.playlist.presentation.PlaylistAction

@Composable
fun SystemPlayListOptionsBottomSheet(
    id: Int,
    title: String,
    songsCount: Int,
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
                Image(
                    painter = painterResource(Res.drawable.ic_heart),
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
            }
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

    }
}

@Composable
@Preview
fun SystemPlayListOptionsBottomSheetPreview() {
    VibePlayerTheme {
        SystemPlayListOptionsBottomSheet(
            id = 1,
            title = "Fav Test",
            songsCount = 1,
            onAction = {},
            modifier = Modifier
        )

    }
}