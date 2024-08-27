package com.sobolevkir.playlistmaker.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sobolevkir.playlistmaker.favorites.data.dao.FavoriteTrackDao
import com.sobolevkir.playlistmaker.favorites.data.entity.FavoriteTrackEntity

@Database(version = 1, entities = [FavoriteTrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun getFavoriteTrackDao(): FavoriteTrackDao
}