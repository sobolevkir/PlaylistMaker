package com.sobolevkir.playlistmaker.tracklist

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)
