package com.sobolevkir.playlistmaker.common.domain.model

data class Playlist(
    val name: String,
    val list: List<Track>
)