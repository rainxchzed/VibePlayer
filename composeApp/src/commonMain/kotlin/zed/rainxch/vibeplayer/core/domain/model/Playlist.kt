package zed.rainxch.vibeplayer.core.domain.model

import kotlinx.collections.immutable.ImmutableList

typealias MusicId = Int

/**
 * @param title is title of the playlist (max length - 40)
 * @param musics represents the list of music id's, and we can fetch musics via these id's and i
 * think we don't need another field representing the number of songs in playlist
 * @param coverImage is used for representing the cover main of playlist (nullable)
 */
data class Playlist(
    val title: String,
    val musics: ImmutableList<MusicId>,
    val coverImage: String? = null
)

/**
 * Lightweight model for displaying playlist in lists
 * @param title is title of the playlist (max length - 40)
 * @param musicCount number of songs in the playlist
 * @param coverImage is used for representing the cover of playlist (nullable)
 */
data class PlaylistInfo(
    val title: String,
    val musicCount: Int,
    val coverImage: String? = null
)