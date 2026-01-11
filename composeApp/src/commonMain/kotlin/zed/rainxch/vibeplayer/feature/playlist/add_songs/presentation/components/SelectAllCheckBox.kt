package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.select_all

@Composable
fun SelectAllCheckBox(
    checkedState: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.clickable {
        onCheckedChanged(!checkedState)
    }, verticalAlignment = Alignment.CenterVertically) {

        CircleCheckBox(checkedState = checkedState)
    /*    Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (checkedState) MaterialTheme.colorScheme.primary else Color.Transparent
                )
                .border(
                    width = 2.dp,
                    color = if (checkedState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            if (checkedState) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Checked",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }*/

        Text(
            text = stringResource(Res.string.select_all),
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface

        )
    }
}