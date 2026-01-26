package zed.rainxch.vibeplayer.feature.playlist.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
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
import zed.rainxch.vibeplayer.core.presentation.components.buttons.AppOutlinedButton
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme
import zed.rainxch.vibeplayer.core.presentation.utils.ObserveAsEvents
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.PlayListOptionsBottomSheet
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.PlaylistCard
import zed.rainxch.vibeplayer.feature.playlist.presentation.components.PlaylistsHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistRoot(
    onNavigateToAddSongs: (playListId: Int) -> Unit,
    onNavigateToPlaylist: (playListId: Int) -> Unit,
    onShowSnackBar: (message: String) -> Unit,
    viewModel: PlaylistViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is PlaylistEvent.ShowSnackbar -> {
                onShowSnackBar(event.message)
            }

            is PlaylistEvent.OnNavigateToAddSongs -> {
                onNavigateToAddSongs(event.playlistId)
            }
        }
    }

    PlaylistScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToPlaylist = onNavigateToPlaylist,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    state: PlaylistState,
    onAction: (PlaylistAction) -> Unit,
    onNavigateToPlaylist: (playListId: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = bottomSheetState
    )

    LaunchedEffect(state.showBottomSheet) {
        if (state.showBottomSheet == null) {
            if (bottomSheetState.currentValue != SheetValue.Hidden) {
                keyboardController?.hide()
                bottomSheetState.hide()
            }
        } else {

            if (!bottomSheetState.isVisible) {
                bottomSheetState.expand()
            } else
            bottomSheetState.expand()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { bottomSheetState.targetValue }
            .collect { currentValue ->
                if (currentValue == SheetValue.Hidden && state.showBottomSheet != null) {
                    keyboardController?.hide()
                    onAction(PlaylistAction.OnDismissBottomSheet)
                }
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetDragHandle = null,
        sheetContent = {

            when(val sheet = state.showBottomSheet) {
                SheetContent.CreatePlaylist -> {

                    CreateNewPlaylistBottomSheet(
                        playlistName = state.newPlaylistName,
                        onPlaylistNameChange = { name ->
                            onAction(PlaylistAction.OnPlaylistNameChange(name))
                        },
                        onCancel = {
                            scope.launch {
                                bottomSheetState.hide()
                            }
                        },
                        onCreate = {
                            onAction(PlaylistAction.OnConfirmCreatePlaylist)
                        }
                    )
                }
                is SheetContent.ShowPlaylistActions ->{
                    PlayListOptionsBottomSheet(
                        id = sheet.id,
                        title = sheet.title,
                        songsCount = sheet.songsCount,
                        coverImage = sheet.coverImage,
                    )
                }
                null -> {
                    Box(modifier = Modifier.height(1.dp))
                }
            }

            /*CreateNewPlaylistBottomSheet(
                playlistName = state.newPlaylistName,
                onPlaylistNameChange = { name ->
                    onAction(PlaylistAction.OnPlaylistNameChange(name))
                },
                onCancel = {
                    scope.launch {
                        bottomSheetState.hide()
                    }
                },
                onCreate = {
                    onAction(PlaylistAction.OnConfirmCreatePlaylist)
                    onNavigateToAddSongs((state.userPlaylistTotalCount))
                }
            )*/
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        sheetContainerColor = MaterialTheme.colorScheme.onSecondary,
        sheetTonalElevation = 4.dp,
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondary)
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            item {
                PlaylistsHeader(
                    totalCount = state.totalPlaylistCount,
                    onCreatePlaylistClick = {
                        onAction(PlaylistAction.OnCreatePlaylistClick)
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
                            onAction(PlaylistAction.OnCreatePlaylistClick)
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
                        onAction(PlaylistAction.OnPlaylistMoreOptionsClick(
                            playList.id,
                            title = playList.title,
                            songsCount = playList.songsCount,
                            coverImage = playList.coverImage
                        ))
                    }
                )
            }
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
                    favouritesCount = 0,
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
                onNavigateToPlaylist = {},
            )
        }
    }
}