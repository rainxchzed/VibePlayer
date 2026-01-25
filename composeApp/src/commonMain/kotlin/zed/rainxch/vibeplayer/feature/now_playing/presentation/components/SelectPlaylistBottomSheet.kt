package zed.rainxch.vibeplayer.feature.now_playing.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.Bitmap
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource
import vibeplayer.composeapp.generated.resources.Res
import vibeplayer.composeapp.generated.resources.ic_heart
import vibeplayer.composeapp.generated.resources.ic_playlist
import zed.rainxch.vibeplayer.core.data.local.db.AppDatabase
import zed.rainxch.vibeplayer.core.domain.model.Playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPlaylistBottomSheet(
    onDismissRequest: () -> Unit,
    playlists: ImmutableList<Playlist>,
    onCreatePlaylistClick: () -> Unit,
    onFavouritePlaylistClick: () -> Unit,
    onPlaylistSelected: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetMaxWidth = 480.dp,
        containerColor = MaterialTheme.colorScheme.onSecondary,
        dragHandle = null,
        modifier = modifier
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            NowPlayingPlaylistItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xffA7BBD1),
                                        Color(0xffA7BBD1).copy(alpha = .2f),
                                    )
                                )
                            )
                            .padding(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                name = "Create Playlist",
                onClick = onCreatePlaylistClick
            )

            playlists.forEach { playlist ->
                NowPlayingPlaylistItem(
                    icon = {
                        Image(
                            painter = painterResource(if(playlist.id == AppDatabase.FAVOURITES_PLAYLIST_ID) {
                                Res.drawable.ic_heart
                            } else Res.drawable.ic_playlist),
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xffDE84FF),
                                            Color(0xffDE84FF).copy(alpha = .2f),
                                        )
                                    )
                                )
                                .padding(14.dp),
                            contentScale = ContentScale.Crop
                        )
                    },
                    name = playlist.title,
                    songsCount = playlist.musics.count(),
                    onClick = {
                        if(playlist.id == AppDatabase.FAVOURITES_PLAYLIST_ID) {
                            onFavouritePlaylistClick()
                        } else {
                            onPlaylistSelected(playlist)
                        }
                    }
                )
            }
        }
    }
}