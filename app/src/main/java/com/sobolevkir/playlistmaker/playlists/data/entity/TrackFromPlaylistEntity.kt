package com.sobolevkir.playlistmaker.playlists.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_from_playlist_table")
data class TrackFromPlaylistEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id")
    val trackId: Long = -1,
    val trackName: String = "",
    val artistName: String = "",
    val trackTimeMillis: Int = 0,
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