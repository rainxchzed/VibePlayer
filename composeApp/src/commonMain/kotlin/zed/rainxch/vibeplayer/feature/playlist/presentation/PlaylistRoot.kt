package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_heart
import vibeplayer.composeapp.generated.resources.ic_playlist
import vibeplayer.composeapp.generated.resources.ic_plus
import vibeplayer.composeapp.generated.resources.playlists_create_playlist_button
import vibeplayer.composeapp.generated.resources.playlists_favourites_title
import vibeplayer.composeapp.generated.resources.playlists_my_playlists_title
import vibeplayer.composeapp.generated.resources.playlists_total_count_title
import vibeplayer.composeapp.generated.resources.songs_screen_songs_count
import zed.rainxch.vibeplayer.core.presentation.components.buttons.AppOutlinedButton
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.PlaylistCard
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.PlaylistsHeader

@Composable
fun PlaylistRoot(
    viewModel: PlaylistViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PlaylistScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PlaylistScreen(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            PlaylistsHeader(
                totalCount = state.totalCount,
                onCreatePlaylistClick = {
                    onAction(PlaylistAction.OnCreatePlaylistClick)
                }
            )
            PlaylistCard(
                state = PlaylistCardUi(
                    title = stringResource(Res.string.playlists_favourites_title),
                    songsCount = state.favouritesCount,
                ),
                defaultImage = Res.drawable.ic_heart
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                text = stringResource(
                    Res.string.playlists_my_playlists_title,
                    state.userPlaylists.size,
                    state.userPlaylists.size
                ),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (state.userPlaylists.isEmpty()) {
                AppOutlinedButton(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = {
                        onAction(PlaylistAction.OnCreatePlaylistClick)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_plus),
                            contentDescription = "Add playlist",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            modifier = Modifier,
                            text = stringResource(Res.string.playlists_create_playlist_button),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        items(state.userPlaylists) {
            PlaylistCard(
                state = it,
                defaultImage = Res.drawable.ic_playlist
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        Surface {
            PlaylistScreen(
                state = PlaylistState(
                    totalCount = 3,
                    favouritesCount = 0,
                    userPlaylists = listOf(
                        PlaylistCardUi(
                            title = "Test1",
                            songsCount = 10,
                        ),
                        PlaylistCardUi(
                            title = "Test1",
                            songsCount = 10,
                        ),
                        PlaylistCardUi(
                            title = "Test1",
                            songsCount = 10,
                        ),

                    )
                ),
                onAction = {}
            )
        }

    }
}