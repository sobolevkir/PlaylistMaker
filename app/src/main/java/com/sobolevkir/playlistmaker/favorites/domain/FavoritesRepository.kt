package com.sobolevkir.playlistmaker.favorites.domain

import com.sobolevkir.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addTrackToFavorites(track: Track)

    suspend fun removeTrackFromFavorites(trackId: Long)

    fun getFavoriteTracks(): Flow<List<Track>>

}