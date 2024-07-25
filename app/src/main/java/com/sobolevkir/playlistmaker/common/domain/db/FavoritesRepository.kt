package com.sobolevkir.playlistmaker.common.domain.db

import com.sobolevkir.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    suspend fun addTrackToFavorites(track: Track)

    suspend fun removeTrackFromFavorites(track: Track)

    suspend fun isTrackFavorite(trackId: String): Boolean

    fun getFavoriteTracks(): Flow<List<Track>>

}