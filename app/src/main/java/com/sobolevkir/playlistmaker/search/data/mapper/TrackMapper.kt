package com.sobolevkir.playlistmaker.search.data.mapper

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.search.data.dto.TrackDto
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun map(trackListDto: List<TrackDto>): List<Track> {
        return trackListDto.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName ?: "",
                artistName = it.artistName ?: "",
                trackTimeMillis = it.trackTimeMillis ?: 0,
                formattedTrackTime = getFormattedTime(it.trackTimeMillis),
                artworkUrl100 = it.artworkUrl100 ?: "",
                artworkUrl512 = getLargeCoverArtwork(it.artworkUrl100) ?: "",
                collectionName = it.collectionName ?: "",
                releaseYear = getReleaseYear(it.releaseDate) ?: "",
                primaryGenreName = it.primaryGenreName ?: "",
                country = it.country ?: "",
                previewUrl = it.previewUrl ?: "",
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