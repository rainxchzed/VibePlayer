package zed.rainxch.vibeplayer.feature.songs.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_play_outlined
import vibeplayer.composeapp.generated.resources.main_screen_play_button
import vibeplayer.composeapp.generated.resources.main_screen_shuffle_button
import vibeplayer.composeapp.generated.resources.repeat_all
import zed.rainxch.vibeplayer.core.presentation.components.buttons.AppOutlinedButton
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun QuickPlayBar(
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppOutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onShuffleClick,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Res.drawable.repeat_all),
                    contentDescription = null
                )
                Text(
                    text = stringResource(Res.string.main_screen_shuffle_button),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
        AppOutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onPlayClick,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(Res.drawable.ic_play_outlined),
                    contentDescription = null
                )
                Text(
                    text = stringResource(Res.string.main_screen_play_button),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        Surface {
            QuickPlayBar(
                onPlayClick = {},
                onShuffleClick = {}
            )
        }
    }
}
