package zed.rainxch.vibeplayer.core.presentation.components.radioGroup

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    options: List<String>,
    label: String? = null,
    selectedOptionIndex: Int,
    onOptionSelected: (Int) -> Unit,
) {
    Column(
        modifier =  modifier
    ) {
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        Row(
            Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEachIndexed { index, text ->
                val isSelected = index == selectedOptionIndex

                Row(
                    Modifier
                        .weight(1f)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                        .clip(CircleShape)
                        .selectable(
                            selected = isSelected,
                            onClick = { onOptionSelected(index) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = null
                    )
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }

}



@Preview(widthDp = 400)
@Composable
private fun RadioGroupPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    VibePlayerTheme {
        Surface {
            RadioGroup(
                modifier = Modifier.fillMaxWidth(),
                options = listOf("Option 1", "Option 2"),
                selectedOptionIndex = selectedIndex,
                label = "Ignore duration less than",
                onOptionSelected = { index -> selectedIndex = index }
            )
        }

    }
}
