package com.sobolevkir.playlistmaker.common.data.impl

import com.sobolevkir.playlistmaker.common.data.db.AppDatabase
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.data.converter.FavoriteTrackDbConverter
import com.sobolevkir.playlistmaker.common.data.db.entity.FavoriteTrackEntity
import com.sobolevkir.playlistmaker.common.domain.db.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: FavoriteTrackDbConverter
) : FavoritesRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.getFavoriteTrackDao().insertTrack(trackDbConverter.convert(track))
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        appDatabase.getFavoriteTrackDao().deleteTrack(trackDbConverter.convert(track))
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        val favoriteTracksIds = appDatabase.getFavoriteTrackDao().getTracksIds()
        return favoriteTracksIds.contains(trackId)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.getFavoriteTrackDao().getTracks().reversed()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.convert(track) }
    }

}