package com.sobolevkir.playlistmaker.favorites.domain

interface FavoritesInteractor {
    fun addTrackToFavorites(trackId: String)
    fun removeTrackFromFavorites(trackId: String)
    fun isTrackFavorite(trackId: String): Boolean
    fun getSavedFavoritesId(): Set<String>
}