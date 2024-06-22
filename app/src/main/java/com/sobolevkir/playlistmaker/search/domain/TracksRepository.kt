package com.sobolevkir.playlistmaker.search.domain

import com.sobolevkir.playlistmaker.common.util.Resource
import com.sobolevkir.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTrack(searchQueryText: String): Flow<Resource<List<Track>>>
    fun getSavedHistory(): MutableList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}