package com.sobolevkir.playlistmaker.common.domain

interface FavoritesRepository {
    fun addTrackToFavorites(trackId: String)
    fun removeTrackFromFavorites(trackId: String)
    fun isTrackFavorite(trackId: String): Boolean
    fun getSavedFavoritesId(): Set<String>

    companion object {
        const val FAVORITES = "FAVORITES_KEY"
    }
}