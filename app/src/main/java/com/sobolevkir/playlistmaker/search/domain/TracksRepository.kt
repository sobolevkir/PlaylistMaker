package com.sobolevkir.playlistmaker.search.domain

import com.sobolevkir.playlistmaker.common.domain.model.Resource
import com.sobolevkir.playlistmaker.common.domain.model.Track

interface TracksRepository {
    fun searchTrack(searchQueryText: String): Resource<List<Track>>
    fun getSavedHistory(): MutableList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}