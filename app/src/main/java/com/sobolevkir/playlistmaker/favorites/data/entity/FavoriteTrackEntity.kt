package com.sobolevkir.playlistmaker.favorites.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks_table")
data class FavoriteTrackEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "track_id")
    val trackId: Long = -1,
    val trackName: String = "",
    val artistName: String = "",
    val formattedTrackTime: String = "",
    val artworkUrl100: String = "",
    val artworkUrl512: String = "",
    val collectionName: String = "",
    val releaseYear: String = "",
    val primaryGenreName: String = "",
    val country: String = "",
    val previewUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)