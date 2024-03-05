package com.sobolevkir.playlistmaker.creator

import android.app.Application
import com.sobolevkir.playlistmaker.common.data.impl.FavoritesRepositoryImpl
import com.sobolevkir.playlistmaker.common.data.impl.LocalStorageImpl
import com.sobolevkir.playlistmaker.common.data.impl.ResourceProviderImpl
import com.sobolevkir.playlistmaker.common.domain.FavoritesInteractor
import com.sobolevkir.playlistmaker.common.domain.FavoritesRepository
import com.sobolevkir.playlistmaker.common.domain.LocalStorage
import com.sobolevkir.playlistmaker.common.domain.LocalStorage.Companion.PLAYLISTMAKER_PARAMS
import com.sobolevkir.playlistmaker.common.domain.ResourceProvider
import com.sobolevkir.playlistmaker.common.domain.impl.FavoritesInteractorImpl
import com.sobolevkir.playlistmaker.player.data.impl.PlayerImpl
import com.sobolevkir.playlistmaker.player.domain.Player
import com.sobolevkir.playlistmaker.player.domain.PlayerInteractor
import com.sobolevkir.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.sobolevkir.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.sobolevkir.playlistmaker.search.data.network.RetrofitNetworkClient
import com.sobolevkir.playlistmaker.search.domain.TracksInteractor
import com.sobolevkir.playlistmaker.search.domain.TracksRepository
import com.sobolevkir.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.sobolevkir.playlistmaker.settings.data.impl.ExternalNavigatorImpl
import com.sobolevkir.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.sobolevkir.playlistmaker.settings.domain.ExternalNavigator
import com.sobolevkir.playlistmaker.settings.domain.SettingsInteractor
import com.sobolevkir.playlistmaker.settings.domain.SettingsRepository
import com.sobolevkir.playlistmaker.settings.domain.SharingInteractor
import com.sobolevkir.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.sobolevkir.playlistmaker.settings.domain.impl.SharingInteractorImpl

object Creator {

    private lateinit var application: Application

    fun setApplication(application: Application) {
        this.application = application
    }

    private fun provideLocalStorage(): LocalStorage {
        return LocalStorageImpl(
            application.getSharedPreferences(
                PLAYLISTMAKER_PARAMS,
                Application.MODE_PRIVATE
            )
        )
    }

    private fun getResourceProvider(): ResourceProvider = ResourceProviderImpl(application)

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(), getFavoritesRepository())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(application), provideLocalStorage())
    }

    fun providePlayerInteractor(previewUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(getPlayer(previewUrl))
    }

    private fun getPlayer(previewUrl: String): Player = PlayerImpl(previewUrl)

    fun provideFavoritesInteractor(): FavoritesInteractor {
        return FavoritesInteractorImpl(getFavoritesRepository())
    }

    private fun getFavoritesRepository(): FavoritesRepository {
        return FavoritesRepositoryImpl(provideLocalStorage())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(), getResourceProvider())
    }

    private fun getExternalNavigator(): ExternalNavigator = ExternalNavigatorImpl(application)

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(provideLocalStorage(), application)
    }

}