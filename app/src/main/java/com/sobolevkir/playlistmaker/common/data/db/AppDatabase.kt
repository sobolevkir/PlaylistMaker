package com.sobolevkir.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sobolevkir.playlistmaker.common.data.db.dao.FavoriteTrackDao
import com.sobolevkir.playlistmaker.common.data.db.entity.FavoriteTrackEntity

@Database(version = 1, entities = [FavoriteTrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun getFavoriteTrackDao(): FavoriteTrackDao
}