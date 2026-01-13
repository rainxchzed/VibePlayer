package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.add_songs
import vibeplayer.composeapp.generated.resources.ic_arrow_left
import vibeplayer.composeapp.generated.resources.no_results_found
import vibeplayer.composeapp.generated.resources.ok
import vibeplayer.composeapp.generated.resources.search
import vibeplayer.composeapp.generated.resources.songs_added_to_playlist
import zed.rainxch.vibeplayer.core.presentation.components.textFields.PrimaryTextField
import zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation.components.CheckBoxMusicItem
import zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation.components.SelectAllCheckBox
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsState
import zed.rainxch.vibeplayer.feature.songs.presentation.SongsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSongsRoot(
    songsViewModel: SongsViewModel,
    addSongsViewModel: AddSongsViewModel = koinViewModel(),
    playlistId: Int,
    onShowSnackBar: (message: String) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state by songsViewModel.state.collectAsStateWithLifecycle()
    val addSongsState by addSongsViewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        addSongsViewModel.events.collect { event ->
            when (event) {
                is AddSongsEvent.NavigateBack -> {
                    onBackPressed()

                    val songsCount = addSongsState.selectedMusicIds.size
                    val snackBarMessage = getPluralString(
                        resource = Res.plurals.songs_added_to_playlist,
                        quantity = songsCount,
                        songsCount
                    )

                    onShowSnackBar(snackBarMessage)
                }
            }
        }
    }

    LaunchedEffect(addSongsViewModel) {
        addSongsViewModel.onAction(AddSongsAction.OnClearSelection)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            CenterAlignedTopAppBar(
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
            }, title = {
                Text(
                    text = if (addSongsState.selectedMusicIds.isEmpty()) stringResource(Res.string.add_songs) else "${addSongsState.selectedMusicIds.size} Selected",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary
                )
            )

            AddSongsScreen(
                state = state,
                playlistId = playlistId,
                addSongsState = addSongsState,
                onAction = addSongsViewModel::onAction
            )


        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSongsScreen(
    state: SongsState,
    playlistId: Int,
    addSongsState: AddSongsState,
    onAction: (AddSongsAction) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {


            PrimaryTextField(
                value = addSongsState.searchQuery,
                onValueChange = { query ->
                    onAction(AddSongsAction.OnSearchQueryChange(query))
                },
                startIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                },
                endIcon = {
                    if (addSongsState.searchQuery.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onAction(AddSongsAction.OnSearchQueryClearClick)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                placeholder = stringResource(Res.string.search),
                modifier = Modifier.fillMaxWidth().padding(all = 12.dp),
                imeAction = ImeAction.Done

            )

            if (addSongsState.filteredMusic.isEmpty() && addSongsState.searchQuery.isNotBlank()) {
                Text(
                    text = stringResource(Res.string.no_results_found),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                SelectAllCheckBox(
                    checkedState = state.musics.isNotEmpty() && addSongsState.selectedMusicIds.size == state.musics.size,
                    onCheckedChanged = { isSelected ->
                        onAction(AddSongsAction.OnSelectedAllSongs(state.musics, isSelected))
                    },
                    modifier = Modifier.fillMaxWidth().padding(all = 12.dp)
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .padding(all = 12.dp),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 80.dp // Roughly button height (56dp) + padding (24dp)
                )
            ) {
                items(
                    items = addSongsState.filteredMusic,
                    key = { music -> music.id }
                ) { music ->
                    CheckBoxMusicItem(
                        music = music,
                        isSelected = music.id in addSongsState.selectedMusicIds,
                        onClick = { isSelected ->
                            onAction(AddSongsAction.OnSongSelected(music.id, isSelected))
                        }
                    )
                }
            }
        }

        if (addSongsState.selectedMusicIds.isNotEmpty()) {
            Button(
                onClick = {

                    if (!addSongsState.isAddingSongs)
                        onAction(AddSongsAction.OnConfirm(playlistId))

                },
                modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .padding(12.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text = stringResource(Res.string.ok).uppercase())
            }
        }
    }
}