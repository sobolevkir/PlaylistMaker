package com.sobolevkir.playlistmaker.domain.api

import com.sobolevkir.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTrack(searchQueryText: String): List<Track>
}