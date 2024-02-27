package com.sobolevkir.playlistmaker.common.data.impl

import com.sobolevkir.playlistmaker.common.domain.FavoritesRepository
import com.sobolevkir.playlistmaker.common.domain.FavoritesRepository.Companion.FAVORITES
import com.sobolevkir.playlistmaker.common.domain.LocalStorage

class FavoritesRepositoryImpl(private val localStorage: LocalStorage) : FavoritesRepository {
    override fun addTrackToFavorites(trackId: String) {
        changeFavorites(trackId = trackId, remove = false)
    }

    override fun removeTrackFromFavorites(trackId: String) {
        changeFavorites(trackId = trackId, remove = true)
    }

    override fun isTrackFavorite(trackId: String): Boolean =
        getSavedFavoritesId().contains(trackId)

    override fun getSavedFavoritesId(): Set<String> {
        return localStorage.read(FAVORITES, emptySet())
    }

    private fun changeFavorites(trackId: String, remove: Boolean) {
        val mutableFavoritesSet = getSavedFavoritesId().toMutableSet()
        val modified =
            if (remove) mutableFavoritesSet.remove(trackId) else mutableFavoritesSet.add(trackId)
        if (modified) localStorage.write(FAVORITES, mutableFavoritesSet)
    }
}