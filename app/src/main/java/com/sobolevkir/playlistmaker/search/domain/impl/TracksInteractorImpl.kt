package com.sobolevkir.playlistmaker.search.domain.impl

import com.sobolevkir.playlistmaker.common.domain.model.ErrorType
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.util.Resource
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesRepository
import com.sobolevkir.playlistmaker.search.domain.TracksInteractor
import com.sobolevkir.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val tracksRepository: TracksRepository,
    private val favoritesRepository: FavoritesRepository
) : TracksInteractor {

    override fun searchTrack(searchQueryText: String): Flow<Pair<List<Track>?, ErrorType?>> {
        return tracksRepository.searchTrack(searchQueryText).map { result ->
            when (result) {
                is Resource.Success -> {
                    val savedFavoritesId = favoritesRepository.getSavedFavoritesId()
                    val dataWithFavorites = result.data?.map {
                        it.copy(isFavorite = savedFavoritesId.contains(it.trackId))
                    }
                    Pair(dataWithFavorites, null)
                }

                is Resource.Error -> Pair(null, result.errorType)
            }
        }
    }

    override fun getSavedHistory(): MutableList<Track> {
        return tracksRepository.getSavedHistory()
    }

    override fun addTrackToHistory(track: Track) {
        tracksRepository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        tracksRepository.clearHistory()
    }
}