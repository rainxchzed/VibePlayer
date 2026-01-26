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

@Composable
fun PlayListOptionsBottomSheet(
    id: Int,
    title: String,
    songsCount: Int,
    coverImage: String?,
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
            onClick = {},
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
            onClick = {},
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
            onClick = {},
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
            onClick = {},
            modifier = Modifier.fillMaxWidth()
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.onSecondary)

        )
        /* Text(
             text = "Create New Playlist",
             style = MaterialTheme.typography.titleMedium,
             color = MaterialTheme.colorScheme.onSurface
         )

         Spacer(Modifier.height(20.dp))

         PrimaryTextField(
             value = "",
             onValueChange = onPlaylistNameChange,
             placeholder = "Enter playlist name",
             endIcon = {
                 Text(
                     text = "/40",
                     color = MaterialTheme.colorScheme.onSurfaceVariant,
                     style = MaterialTheme.typography.bodySmall
                 )
             },
             imeAction = ImeAction.Done,
             modifier = Modifier.fillMaxWidth()
         )

         Spacer(Modifier.height(20.dp))

         Row(
             modifier = Modifier.fillMaxWidth(),
             verticalAlignment = Alignment.CenterVertically,
             horizontalArrangement = Arrangement.spacedBy(12.dp)
         ) {
             AppOutlinedButton(
                 onClick = onCancel,
                 modifier = Modifier.weight(1f)
             ) {
                 Text(
                     text = "Cancel",
                     style = MaterialTheme.typography.bodyLarge,
                     color = MaterialTheme.colorScheme.onSurface
                 )
             }

             PrimaryButton(
                 text = "Create",
                 onClick = onCreate,
                 modifier = Modifier.weight(1f),
             )
         }*/
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
                coverImage = ""
            ,
            modifier = Modifier
        )

    }
}