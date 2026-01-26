package zed.rainxch.vibeplayer.feature.playlist.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.cancel
import vibeplayer.composeapp.generated.resources.new_name
import vibeplayer.composeapp.generated.resources.rename
import vibeplayer.composeapp.generated.resources.rename_playlist
import zed.rainxch.vibeplayer.core.presentation.components.buttons.AppOutlinedButton
import zed.rainxch.vibeplayer.core.presentation.components.buttons.PrimaryButton
import zed.rainxch.vibeplayer.core.presentation.components.textFields.PrimaryTextField

@Composable
fun RenamePlaylistBottomSheet(
    playlistName: String,
    onCurrentPlaylistNameChange: (String) -> Unit,
    onCancel: () -> Unit,
    onRename: () -> Unit,
    modifier: Modifier = Modifier
)  {
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
        Text(
            text = stringResource(Res.string.rename_playlist),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(20.dp))

        PrimaryTextField(
            value = playlistName,
            onValueChange = onCurrentPlaylistNameChange,
            placeholder = stringResource(Res.string.new_name),
            endIcon = {
                Text(
                    text = "${playlistName.length}/40",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 10.dp)

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
                    text = stringResource(Res.string.cancel),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            PrimaryButton(
                text = stringResource(Res.string.rename),
                onClick = onRename,
                modifier = Modifier.weight(1f),
                enabled = playlistName.isNotBlank() &&
                        playlistName.length <= 40
            )
        }
    }
}