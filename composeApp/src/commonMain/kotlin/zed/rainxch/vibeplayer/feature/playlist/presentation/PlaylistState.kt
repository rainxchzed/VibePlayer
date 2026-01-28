package zed.rainxch.vibeplayer.feature.playlist.presentation

import zed.rainxch.vibeplayer.feature.playlist.presentation.model.PlaylistCardUi

data class PlaylistState(
    val userPlaylistTotalCount: Int = 0,
    val totalPlaylistCount: Int = 0,
    val userPlaylists: List<PlaylistCardUi> = emptyList(),
    val systemPlaylists: List<PlaylistCardUi> = emptyList(),
    val newPlaylistName: String = "",
    val currentPlaylistName: String = "",
    val showImagePickerForPlaylistId: Int? = null
)