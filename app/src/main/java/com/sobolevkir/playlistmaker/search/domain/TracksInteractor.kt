package com.sobolevkir.playlistmaker.search.domain

import com.sobolevkir.playlistmaker.common.domain.model.ErrorType
import com.sobolevkir.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTrack(searchQueryText: String): Flow<Pair<List<Track>?, ErrorType?>>
    fun getSavedHistory(): MutableList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
}