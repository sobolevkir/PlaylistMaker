package com.sobolevkir.playlistmaker.data.dto

data class TracksSearchResponse (val resultCount: Int,
                                 val results: List<TrackDto>) : NetworkResponse()