package com.sobolevkir.playlistmaker.common.domain.impl

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.domain.db.FavoritesInteractor
import com.sobolevkir.playlistmaker.common.domain.db.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {

    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        repository.removeTrackFromFavorites(track)
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        return repository.isTrackFavorite(trackId)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> =
        repository.getFavoriteTracks()

}