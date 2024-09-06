package com.sobolevkir.playlistmaker.playlists.presentation.model

interface PlaylistInfoEvent {

    data object TrackRemovedSuccess: PlaylistInfoEvent

    data object PlaylistRemovedSuccess: PlaylistInfoEvent

    data object PlaylistEditedSuccess: PlaylistInfoEvent

}