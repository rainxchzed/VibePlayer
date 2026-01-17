package zed.rainxch.vibeplayer.feature.playlist.add_songs.presentation

import zed.rainxch.vibeplayer.core.domain.model.Music

interface AddSongsAction {
    data class OnSongSelected(val musicId: Int, val isSelected: Boolean) : AddSongsAction
    data class OnSelectedAllSongs(val musics: List<Music>, val isSelected: Boolean) : AddSongsAction
    data class OnConfirm(val playlistId: Int): AddSongsAction
    data object OnClearSelection : AddSongsAction
    data class OnSearchQueryChange(val query: String) : AddSongsAction
    data object OnSearchQueryClearClick : AddSongsAction

}