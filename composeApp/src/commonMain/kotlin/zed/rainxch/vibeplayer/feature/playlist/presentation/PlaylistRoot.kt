package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_heart
import vibeplayer.composeapp.generated.resources.ic_playlist
import vibeplayer.composeapp.generated.resources.ic_plus
import vibeplayer.composeapp.generated.resources.playlists_create_playlist_button
import vibeplayer.composeapp.generated.resources.playlists_my_playlists_title
import zed.rainxch.vibeplayer.core.presentation.components.CreateNewPlaylistBottomSheet
import zed.rainxch.vibeplayer.core.presentation.components.ImagePicker
import zed.rainxch.vibeplayer.core.presentation.components.buttons.AppOutlinedButton
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.core.presentation.utils.ObserveAsEvents
import zed.rainxch.vibeplayer.feature.playlist.presentation.PlaylistAction.*
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.DeletePlayListBottomSheet
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.PlayListOptionsBottomSheet
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.PlaylistCard
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.PlaylistsHeader
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.RenamePlaylistBottomSheet
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.SystemPlayListOptionsBottomSheet
import zed.rainxch.vibeplayer.feature.playlist.presentation.model.PlaylistCardUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistRoot(
    onNavigateToAddSongs: (playListId: Int) -> Unit,
    onNavigateToPlaylist: (playListId: Int) -> Unit,
    onNavigateToPlaylistPlayback: (playListId: Int, startPlaylistPlayback: Boolean) -> Unit,
    onShowSnackBar: (message: String) -> Unit,
    viewModel: PlaylistViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val bottomSheetContent by viewModel.bottomSheetContent.collectAsStateWithLifecycle()

    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is PlaylistEvent.ShowSnackbar -> {
                onShowSnackBar(event.message)
            }
            is PlaylistEvent.OnNavigateToAddSongs -> {
                onNavigateToAddSongs(event.playlistId)
            }
            is PlaylistEvent.OnNavigateToPlaylistPlayback -> {
                onNavigateToPlaylistPlayback(event.playlistId, event.startPlaylistPlayback)
            }
        }
    }

    PlaylistScreen(
        state = state,
        bottomSheetContent = bottomSheetContent,
        onAction = viewModel::onAction,
        onDismissBottomSheet = viewModel::dismissBottomSheet,
        onNavigateToPlaylist = onNavigateToPlaylist,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    state: PlaylistState,
    bottomSheetContent: SheetContent?,
    onAction: (PlaylistAction) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onNavigateToPlaylist: (playListId: Int) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    LaunchedEffect(bottomSheetContent) {
        if (bottomSheetContent != null) {
            bottomSheetState.show()
        } else {
            keyboardController?.hide()
            bottomSheetState.hide()
        }
    }

    if (bottomSheetContent != null) {
        ModalBottomSheet(
            onDismissRequest = {
                keyboardController?.hide()
                onDismissBottomSheet()
            },
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp
            ),
            containerColor = MaterialTheme.colorScheme.onSecondary,
            tonalElevation = 4.dp,
            dragHandle = null
        ) {
            BottomSheetContentSwitch(
                content = bottomSheetContent,
                state = state,
                onAction = onAction,
                onDismiss = {
                    keyboardController?.hide()
                    onDismissBottomSheet()
                }
            )
        }
    }

    ImagePicker(
        show = state.showImagePickerForPlaylistId != null,
        onImageSelected = { imagePath ->
            state.showImagePickerForPlaylistId?.let { playlistId ->
                onAction(OnCoverImageSelected(playlistId, imagePath))
            } ?: onAction(OnImagePickerDismissed)
        }
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
    ) {
        item {
            PlaylistsHeader(
                totalCount = state.totalPlaylistCount,
                onCreatePlaylistClick = {
                    onAction(OnCreatePlaylistClick)
                }
            )
        }

        items(state.systemPlaylists) { playList ->
            PlaylistCard(
                state = playList,
                defaultImage = Res.drawable.ic_heart,
                onClick = {
                    onNavigateToPlaylist(0)
                },
                onThreeDotsClick = {
                    onAction(
                        OnSystemPlaylistMoreOptions(
                            id = playList.id,
                            title = playList.title,
                            songsCount = playList.songsCount,
                        )
                    )
                }
            )
        }

        item {
            Text(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                text = stringResource(
                    Res.string.playlists_my_playlists_title,
                    state.userPlaylists.size,
                    state.totalPlaylistCount
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
                        onAction(OnCreatePlaylistClick)
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            4.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_plus),
                            contentDescription = "Add playlist",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
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

        items(
            items = state.userPlaylists,
            key = { playlist -> playlist.id }
        ) { playList ->
            PlaylistCard(
                state = playList,
                defaultImage = Res.drawable.ic_playlist,
                onClick = {
                    onNavigateToPlaylist(playList.id)
                },
                onThreeDotsClick = {
                    onAction(
                        OnPlaylistMoreOptions(
                            id = playList.id,
                            title = playList.title,
                            songsCount = playList.songsCount,
                            coverImage = playList.coverImage
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun BottomSheetContentSwitch(
    content: SheetContent,
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
    onDismiss: () -> Unit
) {
    when (content) {
        SheetContent.CreatePlaylist -> {
            CreateNewPlaylistBottomSheet(
                playlistName = state.newPlaylistName,
                onPlaylistNameChange = { name ->
                    onAction(OnPlaylistNameChange(name))
                },
                onCancel = onDismiss,
                onCreate = {
                    onAction(OnConfirmCreatePlaylist)
                }
            )
        }

        is SheetContent.ShowPlaylistActions -> {
            PlayListOptionsBottomSheet(
                id = content.id,
                title = content.title,
                songsCount = content.songsCount,
                coverImage = content.coverImage,
                onAction = onAction
            )
        }

        is SheetContent.RenamePlaylist -> {
            RenamePlaylistBottomSheet(
                playlistName = state.currentPlaylistName,
                onCurrentPlaylistNameChange = { changeName ->
                    onAction(OnCurrentPlaylistNameChange(changeName))
                },
                onCancel = onDismiss,
                onRename = {
                    onAction(OnConfirmRenamePlaylist(content.playListId))
                }
            )
        }

        is SheetContent.DeletePlaylist -> {
            DeletePlayListBottomSheet(
                playlistName = content.playlistName,
                onCancel = onDismiss,
                onDelete = {
                    onAction(OnConfirmDeletePlaylist(content.playListId))
                }
            )
        }

        is SheetContent.ShowSystemPlaylistActions -> {
            SystemPlayListOptionsBottomSheet(
                id = content.id,
                title = content.title,
                songsCount = content.songsCount,
                onAction = onAction
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        Surface {
            PlaylistScreen(
                state = PlaylistState(
                    userPlaylists = listOf(
                        PlaylistCardUi(
                            title = "Test1",
                            songsCount = 10,
                            id = 0
                        ),
                        PlaylistCardUi(
                            title = "Test1",
                            songsCount = 10,
                            id = 0
                        ),
                        PlaylistCardUi(
                            title = "Test1",
                            songsCount = 10,
                            id = 0
                        ),

                        )
                ),
                onAction = {},
                bottomSheetContent = null,
                onDismissBottomSheet = { },
                onNavigateToPlaylist = { }
            )
        }
    }
}