package com.sobolevkir.playlistmaker.media.ui.model

import com.sobolevkir.playlistmaker.common.domain.model.Playlist
import com.sobolevkir.playlistmaker.common.domain.model.Track


sealed interface MediaState {

    object NothingFound: MediaState

    data class PlaylistsContent(
        val playlists: List<Playlist>
    ) : MediaState

    data class FavoritesContent(
        val tracks: List<Track>
    ) : MediaState

}