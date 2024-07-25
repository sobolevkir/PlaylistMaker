package com.sobolevkir.playlistmaker.media.presentation

import com.sobolevkir.playlistmaker.common.domain.model.Playlist

sealed interface PlaylistsState {

    object NothingFound : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

}