package com.sobolevkir.playlistmaker.favorites.domain.impl

import com.sobolevkir.playlistmaker.favorites.domain.FavoritesInteractor
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesRepository

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {
    override fun addTrackToFavorites(trackId: String) {
        repository.addTrackToFavorites(trackId)
    }

    override fun removeTrackFromFavorites(trackId: String) {
        repository.removeTrackFromFavorites(trackId)
    }

    override fun isTrackFavorite(trackId: String): Boolean = repository.isTrackFavorite(trackId)
    override fun getSavedFavoritesId(): Set<String> = repository.getSavedFavoritesId()
}