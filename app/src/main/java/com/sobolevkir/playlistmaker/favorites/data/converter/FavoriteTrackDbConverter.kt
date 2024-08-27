package com.sobolevkir.playlistmaker.favorites.data.converter

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.favorites.data.entity.FavoriteTrackEntity

class FavoriteTrackDbConverter {

    fun convert(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
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

    fun convert(track: FavoriteTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.formattedTrackTime,
            track.artworkUrl100,
            track.artworkUrl512,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            isFavorite = true
        )
    }

}