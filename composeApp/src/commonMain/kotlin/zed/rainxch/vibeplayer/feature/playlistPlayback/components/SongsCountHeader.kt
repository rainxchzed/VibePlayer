package zed.rainxch.vibeplayer.feature.playlist.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_plus
import vibeplayer.composeapp.generated.resources.playlists_songs_count
import vibeplayer.composeapp.generated.resources.playlists_total_count_title
import vibeplayer.composeapp.generated.resources.songs_screen_songs_count
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun SongsCountHeader(
    onAddClick: () -> Unit,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.weight(1f).padding(end = 4.dp),
            text = pluralStringResource(
                Res.plurals.playlists_songs_count,
                totalCount,
                totalCount
            ),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        IconButton(
            modifier = Modifier.size(36.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryFixed,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = CircleShape,
            onClick = onAddClick
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_plus),
                contentDescription = "Add playlist",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        Surface {
            PlaylistsHeader(
                onCreatePlaylistClick = {},
                totalCount = 3
            )
        }
    }
}