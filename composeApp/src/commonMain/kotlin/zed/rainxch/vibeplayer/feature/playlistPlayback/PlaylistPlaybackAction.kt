package zed.rainxch.vibeplayer.feature.playlistPlayback

sealed interface PlaylistPlaybackAction {
    data object NavigateBack: PlaylistPlaybackAction
    data object AddSongs: PlaylistPlaybackAction
    data object Play: PlaylistPlaybackAction
    data class PlayMusicWithId(val musicId: Int): PlaylistPlaybackAction
    data object Shuffle: PlaylistPlaybackAction
}