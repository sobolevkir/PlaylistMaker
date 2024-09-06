package com.sobolevkir.playlistmaker.favorites.data.converter

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.favorites.data.entity.FavoriteTrackEntity

class FavoriteTrackDbConverter {

    fun convert(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.formattedTrackTime,
            track.artworkUrl100,
            track.artworkUrl512,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun convert(favoriteTrackEntity: FavoriteTrackEntity): Track {
        return Track(
            favoriteTrackEntity.trackId,
            favoriteTrackEntity.trackName,
            favoriteTrackEntity.artistName,
            favoriteTrackEntity.trackTimeMillis,
            favoriteTrackEntity.formattedTrackTime,
            favoriteTrackEntity.artworkUrl100,
            favoriteTrackEntity.artworkUrl512,
            favoriteTrackEntity.collectionName,
            favoriteTrackEntity.releaseYear,
            favoriteTrackEntity.primaryGenreName,
            favoriteTrackEntity.country,
            favoriteTrackEntity.previewUrl,
            isFavorite = true
        )
    }

}