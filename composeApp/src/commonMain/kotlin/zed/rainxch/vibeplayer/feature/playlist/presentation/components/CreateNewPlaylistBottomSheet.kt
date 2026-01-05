package zed.rainxch.vibeplayer.feature.playlist.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import zed.rainxch.vibeplayer.core.presentation.components.buttons.AppOutlinedButton
import zed.rainxch.vibeplayer.core.presentation.components.buttons.PrimaryButton
import zed.rainxch.vibeplayer.core.presentation.components.textFields.PrimaryTextField
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPlaylistBottomSheet(
    playlistName: String,
    onPlaylistNameChange: (String) -> Unit,
    onCreatePlaylistClick: () -> Unit,
    onDismissRequest: () -> Unit,
    maxCharacters: Int = 40,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetMaxWidth = 480.dp,
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
        ),
        containerColor = MaterialTheme.colorScheme.onSecondary,
        tonalElevation = 4.dp,
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 24.dp,
                horizontal = 16.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create New Playlist",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(20.dp))

            PrimaryTextField(
                value = playlistName,
                onValueChange = onPlaylistNameChange,
                placeholder = "Enter playlist name",
                endIcon = {
                    Text(
                        text = "${playlistName.length}/$maxCharacters",
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
                    onClick = {
                        onDismissRequest()
                    },
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
                    onClick = {
                        onCreatePlaylistClick()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = playlistName.isNotBlank() &&
                            playlistName.length <= maxCharacters
                )

            }
        }
    }
}

@Preview
@Composable
fun CreateNewPlaylistBottomSheetPreview() {
    VibePlayerTheme {
        CreateNewPlaylistBottomSheet(
            onCreatePlaylistClick = {

            },
            onDismissRequest = {

            },
            onPlaylistNameChange = {

            },
            playlistName = ""
        )
    }
}