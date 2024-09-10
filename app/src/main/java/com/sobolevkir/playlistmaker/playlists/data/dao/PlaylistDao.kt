package com.sobolevkir.playlistmaker.playlists.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sobolevkir.playlistmaker.playlists.data.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("DELETE FROM playlists_table WHERE id = :playlistId")
    suspend fun removePlaylist(playlistId: Long): Int

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity): Int

    @Query("SELECT * FROM playlists_table WHERE id = :id")
    fun getPlaylistById(id: Long): Flow<PlaylistEntity?>

    @Query("SELECT name FROM playlists_table")
    fun getPlaylistNames(): Flow<List<String>>

    @Query("SELECT * FROM playlists_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

}