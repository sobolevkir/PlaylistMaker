package com.sobolevkir.playlistmaker.playlists.domain.model;

import com.sobolevkir.playlistmaker.common.domain.model.Track

data class PlaylistWithTracks(
    val id: Long = 0L,
    val name: String = "",
    val description: String = "",
    val coverUri: String = "",
    val tracks: List<Track> = listOf(),
    val totalMinutesDuration: Int = 0,
    val tracksNumber: Int = 0
)
