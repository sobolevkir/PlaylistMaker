package com.sobolevkir.playlistmaker.search.domain

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    fun searchTrack(searchQueryText: String): Flow<Resource<List<Track>>>

    suspend fun getSavedHistory(): List<Track>

    suspend fun addTrackToHistory(track: Track)

    fun clearHistory()

}