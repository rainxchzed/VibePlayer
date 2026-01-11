package zed.rainxch.vibeplayer.feature.main_controller.presentation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import org.jetbrains.compose.ui.tooling.preview.Preview
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.feature.main_controller.presentation.model.MainControllerTabs
import zed.rainxch.vibeplayer.feature.playlist.presentation.PlaylistRoot
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsRoot

@Composable
fun SharedTransitionScope.MainControllerRoot(
    onShowSnackbar: (message: String) -> Unit,
    onNavigateToNowPlaying: (musicId: Int) -> Unit,
    onExpandPlayer: () -> Unit,
    onNavigateToAddSongs: (playListId: Int) -> Unit,
    viewModel: MainControllerViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MainControllerScreen(
        state = state,
        onAction = viewModel::onAction,
        onShowSnackbar = onShowSnackbar,
        onNavigateToNowPlaying = onNavigateToNowPlaying,
        onExpandPlayer = onExpandPlayer,
        onNavigateToAddSongs = onNavigateToAddSongs
    )
}

@Composable
fun SharedTransitionScope.MainControllerScreen(
    state: MainControllerState,
    onAction: (MainControllerAction) -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    onNavigateToNowPlaying: (musicId: Int) -> Unit,
    onExpandPlayer: () -> Unit,
    onNavigateToAddSongs: (playListId: Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
    ) {
        SecondaryTabRow(
            selectedTabIndex = state.selectedTab.index,
            tabs = {
                MainControllerTabs.entries.forEach { tab ->
                    Tab(
                        selected = state.selectedTab.index == tab.index,
                        onClick = {
                            onAction(MainControllerAction.OnSwitchTab(tab))
                        },
                        text = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.onSurface,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
            containerColor = Color.Transparent,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(state.selectedTab.index, matchContentSize = false),
                    height = 2.dp,
                    width = 36.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(
                        topStart = 100.dp,
                        topEnd = 100.dp,
                    )
                )
            }
        )

        when (state.selectedTab) {
            MainControllerTabs.Songs -> {
                SongsRoot(
                    onNavigateToNowPlaying = onNavigateToNowPlaying,
                    onExpandPlayer = onExpandPlayer,
                    sharedTransitionScope = this@MainControllerScreen,
                    animatedContentScope = LocalNavAnimatedContentScope.current
                )
            }

            MainControllerTabs.Playlist -> {
                PlaylistRoot(
                    onNavigateToAddSongs = onNavigateToAddSongs,
                    onShowSnackbar = onShowSnackbar
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        SharedTransitionScope {
            MainControllerScreen(
                state = MainControllerState(),
                onAction = {},
                onNavigateToAddSongs = {},
                onShowSnackbar = {},
                onNavigateToNowPlaying = {},
                onExpandPlayer = {}
            )
        }
    }
}