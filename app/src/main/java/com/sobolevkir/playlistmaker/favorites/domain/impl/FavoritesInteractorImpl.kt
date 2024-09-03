package com.sobolevkir.playlistmaker.favorites.domain.impl

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesInteractor
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {

    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        repository.removeTrackFromFavorites(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> =
        repository.getFavoriteTracks()

}