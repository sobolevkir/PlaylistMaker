package com.sobolevkir.playlistmaker.util

import android.app.Application
import com.sobolevkir.playlistmaker.data.local.LocalStorageImpl
import com.sobolevkir.playlistmaker.data.network.RetrofitNetworkClient
import com.sobolevkir.playlistmaker.data.repository.HistoryRepositoryImpl
import com.sobolevkir.playlistmaker.data.repository.PlayerRepositoryImpl
import com.sobolevkir.playlistmaker.data.repository.TracksRepositoryImpl
import com.sobolevkir.playlistmaker.domain.api.HistoryInteractor
import com.sobolevkir.playlistmaker.domain.api.HistoryRepository
import com.sobolevkir.playlistmaker.domain.api.LocalStorage
import com.sobolevkir.playlistmaker.domain.api.LocalStorage.Companion.PREFS_NAME
import com.sobolevkir.playlistmaker.domain.api.PlayerInteractor
import com.sobolevkir.playlistmaker.domain.api.PlayerRepository
import com.sobolevkir.playlistmaker.domain.api.TracksInteractor
import com.sobolevkir.playlistmaker.domain.api.TracksRepository
import com.sobolevkir.playlistmaker.domain.impl.HistoryInteractorImpl
import com.sobolevkir.playlistmaker.domain.impl.PlayerInteractorImpl
import com.sobolevkir.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private lateinit var application: Application

    fun setApplication(application: Application) {
        this.application = application
    }

    fun provideLocalStorage(): LocalStorage {
        return LocalStorageImpl(
            application.getSharedPreferences(
                PREFS_NAME,
                Application.MODE_PRIVATE
            )
        )
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getHistoryRepository(localStorage: LocalStorage): HistoryRepository {
        return HistoryRepositoryImpl(localStorage)
    }

    fun provideHistoryInteractor(localStorage: LocalStorage): HistoryInteractor {
        return HistoryInteractorImpl(getHistoryRepository(localStorage))
    }

    private fun getPlayerRepository(trackPreviewUrl: String): PlayerRepository {
        return PlayerRepositoryImpl(trackPreviewUrl)
    }

    fun providePlayerInteractor(trackPreviewUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(trackPreviewUrl))
    }

}