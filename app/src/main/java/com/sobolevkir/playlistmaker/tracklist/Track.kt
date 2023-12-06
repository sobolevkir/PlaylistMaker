package com.sobolevkir.playlistmaker.tracklist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale
@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
): Parcelable {
    fun getFormattedTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
    fun getLargeCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
    fun getReleaseYear() = releaseDate?.substring(0..3)
}
