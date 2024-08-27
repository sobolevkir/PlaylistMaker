package com.sobolevkir.playlistmaker.favorites.data.impl

import com.sobolevkir.playlistmaker.favorites.data.converter.FavoriteTrackDbConverter
import com.sobolevkir.playlistmaker.common.data.AppDatabase
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesRepository
import com.sobolevkir.playlistmaker.common.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: FavoriteTrackDbConverter
) : FavoritesRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.getFavoriteTrackDao().insertTrack(trackDbConverter.convert(track))
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        appDatabase.getFavoriteTrackDao().removeTrack(trackDbConverter.convert(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.getFavoriteTrackDao().getTracks()
            .map { tracks -> tracks.map { trackDbConverter.convert(it) } }
    }

}