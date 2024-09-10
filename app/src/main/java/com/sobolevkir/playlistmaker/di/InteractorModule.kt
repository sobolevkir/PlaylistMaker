package com.sobolevkir.playlistmaker.di

import com.sobolevkir.playlistmaker.favorites.domain.FavoritesInteractor
import com.sobolevkir.playlistmaker.favorites.domain.impl.FavoritesInteractorImpl
import com.sobolevkir.playlistmaker.player.domain.PlayerInteractor
import com.sobolevkir.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import com.sobolevkir.playlistmaker.playlists.domain.impl.PlaylistsInteractorImpl
import com.sobolevkir.playlistmaker.search.domain.TracksInteractor
import com.sobolevkir.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.sobolevkir.playlistmaker.settings.domain.SettingsInteractor
import com.sobolevkir.playlistmaker.settings.domain.SharingInteractor
import com.sobolevkir.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.sobolevkir.playlistmaker.settings.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<FavoritesInteractor> { FavoritesInteractorImpl(get()) }

    factory<TracksInteractor> { TracksInteractorImpl(get()) }

    factory<PlaylistsInteractor> { PlaylistsInteractorImpl(get(), get(), get()) }

    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }

    factory<SettingsInteractor> { SettingsInteractorImpl(get()) }

    factory<SharingInteractor> { SharingInteractorImpl(get(), get()) }

}