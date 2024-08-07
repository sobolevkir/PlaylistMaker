package com.sobolevkir.playlistmaker.common.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sobolevkir.playlistmaker.common.data.db.entity.FavoriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete
    suspend fun removeTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks_table ORDER BY timestamp DESC")
    fun getTracks(): Flow<List<FavoriteTrackEntity>>

    @Query("SELECT track_id FROM favorite_tracks_table")
    fun getTrackIds(): Flow<List<Long>>

}