package com.sobolevkir.playlistmaker.search.domain.impl

import com.sobolevkir.playlistmaker.common.domain.FavoritesRepository
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.search.domain.TracksInteractor
import com.sobolevkir.playlistmaker.search.domain.TracksRepository
import com.sobolevkir.playlistmaker.search.domain.model.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val tracksRepository: TracksRepository,
    private val favoritesRepository: FavoritesRepository
) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(searchQueryText: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = tracksRepository.searchTrack(searchQueryText)) {
                is Resource.Success -> {
                    val savedFavoritesId = favoritesRepository.getSavedFavoritesId()
                    val dataWithFavorites = resource.data?.map {
                        it.copy(isFavorite = savedFavoritesId.contains(it.trackId))
                    }
                    consumer.consume(dataWithFavorites, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.errorType)
                }
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