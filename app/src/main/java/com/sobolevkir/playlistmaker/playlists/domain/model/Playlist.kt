package com.sobolevkir.playlistmaker.playlists.domain.model

import com.sobolevkir.playlistmaker.common.domain.model.Track

data class Playlist(
    val name: String,
    val list: List<Track>
)