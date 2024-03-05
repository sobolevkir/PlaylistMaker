package com.sobolevkir.playlistmaker.di

import com.sobolevkir.playlistmaker.common.domain.FavoritesInteractor
import com.sobolevkir.playlistmaker.common.domain.impl.FavoritesInteractorImpl
import com.sobolevkir.playlistmaker.player.domain.PlayerInteractor
import com.sobolevkir.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.sobolevkir.playlistmaker.search.domain.TracksInteractor
import com.sobolevkir.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.sobolevkir.playlistmaker.settings.domain.SettingsInteractor
import com.sobolevkir.playlistmaker.settings.domain.SharingInteractor
import com.sobolevkir.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.sobolevkir.playlistmaker.settings.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<FavoritesInteractor> { FavoritesInteractorImpl(get()) }
    single<TracksInteractor> { TracksInteractorImpl(get(), get()) }
    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }
    single<SettingsInteractor> { SettingsInteractorImpl(get()) }
    single<SharingInteractor> { SharingInteractorImpl(get(), get()) }
}