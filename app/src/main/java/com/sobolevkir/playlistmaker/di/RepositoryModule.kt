package com.sobolevkir.playlistmaker.di

import com.sobolevkir.playlistmaker.favorites.data.converter.FavoriteTrackDbConverter
import com.sobolevkir.playlistmaker.favorites.data.impl.FavoritesRepositoryImpl
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesRepository
import com.sobolevkir.playlistmaker.player.data.impl.PlayerImpl
import com.sobolevkir.playlistmaker.player.domain.Player
import com.sobolevkir.playlistmaker.playlists.data.converter.PlaylistDbConverter
import com.sobolevkir.playlistmaker.playlists.data.impl.PlaylistsRepositoryImpl
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsRepository
import com.sobolevkir.playlistmaker.search.data.impl.TracksRepositoryImpl
import com.sobolevkir.playlistmaker.search.domain.TracksRepository
import com.sobolevkir.playlistmaker.settings.data.impl.ExternalNavigatorImpl
import com.sobolevkir.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.sobolevkir.playlistmaker.settings.domain.ExternalNavigator
import com.sobolevkir.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get()) }

    single<TracksRepository> { TracksRepositoryImpl(get(), get(), get(), get()) }

    single<PlaylistsRepository> { PlaylistsRepositoryImpl(androidContext(), get(), get()) }

    single<Player> { PlayerImpl(get()) }

    single<SettingsRepository> { SettingsRepositoryImpl(get(), get()) }

    single<ExternalNavigator> { ExternalNavigatorImpl(get()) }

    single { FavoriteTrackDbConverter() }

    single { PlaylistDbConverter(get()) }

}