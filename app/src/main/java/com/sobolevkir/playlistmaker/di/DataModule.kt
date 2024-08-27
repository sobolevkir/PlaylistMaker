package com.sobolevkir.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.sobolevkir.playlistmaker.common.data.AppDatabase
import com.sobolevkir.playlistmaker.common.data.impl.LocalStorageImpl
import com.sobolevkir.playlistmaker.common.data.impl.ResourceProviderImpl
import com.sobolevkir.playlistmaker.common.domain.LocalStorage
import com.sobolevkir.playlistmaker.common.domain.ResourceProvider
import com.sobolevkir.playlistmaker.search.data.network.ITunesApiService
import com.sobolevkir.playlistmaker.search.data.network.NetworkClient
import com.sobolevkir.playlistmaker.search.data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<LocalStorage> {
        LocalStorageImpl(
            androidContext().getSharedPreferences(
                LocalStorage.PLAYLISTMAKER_PARAMS,
                Context.MODE_PRIVATE
            )
        )
    }

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl(ITunesApiService.ITUNES_API_BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<NetworkClient> { RetrofitNetworkClient(get(), get()) }

    single<ResourceProvider> { ResourceProviderImpl(get()) }

    single { MediaPlayer() }

    factory { Gson() }

}