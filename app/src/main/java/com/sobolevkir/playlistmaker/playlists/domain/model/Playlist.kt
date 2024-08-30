package com.sobolevkir.playlistmaker.playlists.domain.model

data class Playlist(
    val id: Long = 0L,
    val name: String = "",
    val description: String = "",
    val coverUri: String = "",
    val trackIds: List<Long> = listOf(),
)