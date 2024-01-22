package com.sobolevkir.playlistmaker.data.mapper

import com.sobolevkir.playlistmaker.data.dto.TrackDto
import com.sobolevkir.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun map(trackListDto: List<TrackDto>): List<Track> {
        return trackListDto.map {
            Track(
                it.trackId,
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