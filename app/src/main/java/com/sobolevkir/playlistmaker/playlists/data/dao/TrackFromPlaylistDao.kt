package com.sobolevkir.playlistmaker.playlists.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sobolevkir.playlistmaker.playlists.data.entity.TrackFromPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackFromPlaylistDao {

    @Insert(entity = TrackFromPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackFromPlaylistEntity)

    @Query("DELETE FROM tracks_from_playlist_table WHERE id = :trackId")
    suspend fun removeTrack(trackId: Long)

    @Query("SELECT * FROM tracks_from_playlist_table WHERE id IN (:trackIds) ORDER BY timestamp DESC")
    fun getTracksFromPlaylistByIds(trackIds: List<Long>): Flow<List<TrackFromPlaylistEntity>>

}