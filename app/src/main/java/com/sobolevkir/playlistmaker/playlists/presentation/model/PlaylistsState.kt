package com.sobolevkir.playlistmaker.playlists.presentation.model

import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist

sealed interface PlaylistsState {

    data object Loading: PlaylistsState
    data object NothingFound : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

}