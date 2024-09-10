package com.sobolevkir.playlistmaker.favorites.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sobolevkir.playlistmaker.favorites.data.entity.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Query("DELETE FROM favorite_tracks_table WHERE track_id = :trackId")
    suspend fun removeTrack(trackId: Long)

    @Query("SELECT * FROM favorite_tracks_table ORDER BY timestamp DESC")
    fun getTracks(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT track_id FROM favorite_tracks_table")
    fun getTrackIds(): Flow<List<Long>>

}