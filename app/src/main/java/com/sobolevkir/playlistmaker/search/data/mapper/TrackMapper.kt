package com.sobolevkir.playlistmaker.search.data.mapper

import com.sobolevkir.playlistmaker.search.data.model.TrackDto
import com.sobolevkir.playlistmaker.common.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun map(trackListDto: List<TrackDto>, savedFavoritesId: Set<String>): List<Track> {
        return trackListDto.map {
            Track(
                it.trackId.toString(),
                it.trackName ?: "",
                it.artistName ?: "",
                getFormattedTime(it.trackTimeMillis),
                it.artworkUrl100 ?: "",
                getLargeCoverArtwork(it.artworkUrl100) ?: "",
                it.collectionName ?: "",
                getReleaseYear(it.releaseDate) ?: "",
                it.primaryGenreName ?: "",
                it.country ?: "",
                it.previewUrl ?: "",
                savedFavoritesId.contains(it.trackId.toString()),
            )
        }
    }

    private fun getFormattedTime(timeInMillis: Int?): String {
        return if (timeInMillis != null) {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeInMillis)
        } else ""
    }

    private fun getLargeCoverArtwork(artworkUrl: String?) =
        artworkUrl?.replaceAfterLast('/', "512x512bb.jpg")

    private fun getReleaseYear(releaseDate: String?) = releaseDate?.substring(0..3)
}