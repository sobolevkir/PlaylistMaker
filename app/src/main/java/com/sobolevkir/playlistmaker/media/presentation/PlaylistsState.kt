package com.sobolevkir.playlistmaker.media.presentation

import com.sobolevkir.playlistmaker.common.domain.model.Playlist

sealed interface PlaylistsState {

    data object NothingFound : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

}