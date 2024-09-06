package com.sobolevkir.playlistmaker.search.domain.impl

import com.sobolevkir.playlistmaker.search.domain.model.ErrorType
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.util.Resource
import com.sobolevkir.playlistmaker.search.domain.TracksInteractor
import com.sobolevkir.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val tracksRepository: TracksRepository) : TracksInteractor {

    override fun searchTrack(searchQueryText: String): Flow<Pair<List<Track>?, ErrorType?>> {
        return tracksRepository.searchTrack(searchQueryText).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> Pair(null, result.errorType)
            }
        }
    }

    override suspend fun getSavedHistory(): List<Track> = tracksRepository.getSavedHistory()

    override suspend fun addTrackToHistory(track: Track) {
        tracksRepository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        tracksRepository.clearHistory()
    }
}