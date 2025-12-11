package zed.rainxch.vibeplayer.core.presentation.components.topbars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_arrow_left

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackNavButtonTopBar(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {

        },
        navigationIcon = {
            IconButton(
                onClick = onBackPressed,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryFixed,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_left),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    )
}