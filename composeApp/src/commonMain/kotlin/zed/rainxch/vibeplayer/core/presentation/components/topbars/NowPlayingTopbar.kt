package zed.rainxch.vibeplayer.core.presentation.components.topbars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.cd_minimize
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingTopbar(
    modifier: Modifier = Modifier,
    onMinimizeClick: () -> Unit = {},
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onMinimizeClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryFixed,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(start = 10.dp, top = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown, // Your downward arrow
                    contentDescription = stringResource(Res.string.cd_minimize)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        ),
        title = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    )
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        NowPlayingTopbar(

        )
    }
}
