package com.sobolevkir.playlistmaker.common.domain.db

import com.sobolevkir.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    suspend fun addTrackToFavorites(track: Track)

    suspend fun removeTrackFromFavorites(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>

}