package com.sobolevkir.playlistmaker.playlists.presentation.model

interface PlaylistInfoEvent {

    data class TrackRemovedSuccess(val trackName: String) : PlaylistInfoEvent

    data class PlaylistRemovedSuccess(val playlistName: String) : PlaylistInfoEvent

    data class PlaylistEditedSuccess(val playlistName: String) : PlaylistInfoEvent

    data object UnsuccessfulSharing : PlaylistInfoEvent

}