package com.sobolevkir.playlistmaker.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sobolevkir.playlistmaker.favorites.data.dao.FavoriteTrackDao
import com.sobolevkir.playlistmaker.favorites.data.entity.FavoriteTrackEntity
import com.sobolevkir.playlistmaker.playlists.data.dao.PlaylistDao
import com.sobolevkir.playlistmaker.playlists.data.dao.TrackFromPlaylistDao
import com.sobolevkir.playlistmaker.playlists.data.entity.PlaylistEntity
import com.sobolevkir.playlistmaker.playlists.data.entity.TrackFromPlaylistEntity

@Database(version = 2, entities = [FavoriteTrackEntity::class, PlaylistEntity::class, TrackFromPlaylistEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun getFavoriteTrackDao(): FavoriteTrackDao

    abstract fun getPlaylistDao(): PlaylistDao

    abstract fun getTrackFromPlaylistDao(): TrackFromPlaylistDao

}