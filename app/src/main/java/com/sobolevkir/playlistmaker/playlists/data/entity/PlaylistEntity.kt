package com.sobolevkir.playlistmaker.playlists.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String = "",
    val description: String = "",
    val coverUri: String = "",
    val trackIds: String = ""
)