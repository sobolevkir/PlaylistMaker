package com.sobolevkir.playlistmaker.common.domain.model

import java.io.Serializable

data class Track(
    val trackId: Long = -1,
    val trackName: String = "",
    val artistName: String = "",
    val formattedTrackTime: String = "",
    val artworkUrl100: String = "",
    val artworkUrl512: String = "",
    val collectionName: String = "",
    val releaseYear: String = "",
    val primaryGenreName: String = "",
    val country: String = "",
    val previewUrl: String = "",
    val isFavorite: Boolean = false
) : Serializable
