package zed.rainxch.vibeplayer.feature.playlist.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.play
import vibeplayer.composeapp.generated.resources.playlist_play
import zed.rainxch.vibeplayer.core.presentation.theme.ButtonHover
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun PlaylistActionItem(
    icon: @Composable () -> Unit,
    actionName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.background(ButtonHover, CircleShape).size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Text(
            text = actionName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PlaylistActionItemPreview() {
    VibePlayerTheme {
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
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.onSecondary)
        )

    }
}
