package com.sobolevkir.playlistmaker.search.data.dto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : NetworkResponse()