package com.sobolevkir.playlistmaker.playlists.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sobolevkir.playlistmaker.playlists.data.entity.TrackFromPlaylistEntity

@Dao
interface TrackFromPlaylistDao {

    @Insert(entity = TrackFromPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackFromPlaylistEntity): Long

}