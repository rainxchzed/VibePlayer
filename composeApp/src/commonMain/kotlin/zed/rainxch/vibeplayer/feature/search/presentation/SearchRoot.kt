package zed.rainxch.vibeplayer.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.core.presentation.components.MusicItem
import zed.rainxch.vibeplayer.core.presentation.components.textFields.PrimaryTextField
import zed.rainxch.vibeplayer.core.presentation.theme.VibePlayerTheme

@Composable
fun SearchRoot(
    onBackClick: () -> Unit,
    onNavigateToNowPlayingScreen: (music: Music) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onAction = { action ->
            when (action) {
                SearchAction.OnCancelClick -> {
                    onBackClick()
                }

                is SearchAction.OnMusicClick -> {
                    onNavigateToNowPlayingScreen(action.music)
                }

                else -> {
                    viewModel.onAction(action)
                }
            }
        }
    )
}

@Composable
fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        SearchTopbar(state, onAction)

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.musics) { music ->
                MusicItem(
                    music = music,
                    onClick = {
                        onAction(SearchAction.OnMusicClick(music))
                    }
                )
            }

            item {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (state.musics.isEmpty() && state.searchQuery.isNotBlank()) {
                item {
                    Text(
                        text = "No results found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTopbar(
    state: SearchState,
    onAction: (SearchAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PrimaryTextField(
            value = state.searchQuery,
            onValueChange = { query ->
                onAction(SearchAction.OnSearchQueryChange(query))
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
                if (state.searchQuery.isNotBlank()) {
                    IconButton(
                        onClick = {
                            onAction(SearchAction.OnSearchQueryClearClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.inverseOnSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            placeholder = "Search",
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
                .onFirstVisible {
                    focusManager.moveFocus(FocusDirection.Up)
                }
        )

        TextButton(
            onClick = {
                onAction(SearchAction.OnCancelClick)
            }
        ) {
            Text(
                text = "Cancel",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    VibePlayerTheme {
        SearchScreen(
            state = SearchState(),
            onAction = {}
        )
    }
}