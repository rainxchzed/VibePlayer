package zed.rainxch.vibeplayer.feature.now_playing.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import zed.rainxch.vibeplayer.core.domain.model.Music
import zed.rainxch.vibeplayer.feature.main.presentation.MainState
import zed.rainxch.vibeplayer.feature.main.presentation.MainViewModel
import zed.rainxch.vibeplayer.feature.now_playing.presentation.components.MusicContentItem

@Composable
fun NowPlayingRoot(musicId: Int, viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedMusic by viewModel.selectedMusic.collectAsStateWithLifecycle()

    LaunchedEffect(musicId) {
        viewModel.loadSelectedMusic(musicId)
    }

    NowPlayingScreen(state = state, selectedMusic = selectedMusic)
}

@Composable
fun NowPlayingScreen(
    state: MainState,
    selectedMusic: Music?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        if (selectedMusic != null)
            MusicContentItem(selectedMusic, modifier = Modifier.align(Alignment.Center))

        Column(
            modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.Blue)
                .align(Alignment.BottomCenter)
        ) {

        }
    }
}
