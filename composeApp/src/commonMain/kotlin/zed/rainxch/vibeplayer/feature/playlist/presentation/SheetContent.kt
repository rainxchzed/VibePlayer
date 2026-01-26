package zed.rainxch.vibeplayer.feature.playlist.presentation

sealed interface SheetContent {
    data object CreatePlaylist : SheetContent
    data class ShowPlaylistActions(
        val id: Int,
        val title: String,
        val songsCount: Int,
        val coverImage: String? = null,
    ) : SheetContent

    data class RenamePlaylist(val playListId: Int) : SheetContent
}

