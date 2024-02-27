package com.sobolevkir.playlistmaker.search.data.network

import com.sobolevkir.playlistmaker.search.data.model.TrackDto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : NetworkResponse()