package com.sobolevkir.playlistmaker.di

import com.sobolevkir.playlistmaker.favorites.presentation.FavoritesViewModel
import com.sobolevkir.playlistmaker.player.presentation.PlayerViewModel
import com.sobolevkir.playlistmaker.playlists.presentation.PlaylistCreateViewModel
import com.sobolevkir.playlistmaker.playlists.presentation.PlaylistInfoViewModel
import com.sobolevkir.playlistmaker.playlists.presentation.PlaylistsViewModel
import com.sobolevkir.playlistmaker.search.presentation.SearchViewModel
import com.sobolevkir.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { SearchViewModel(get()) }

    viewModel { params ->
        PlayerViewModel(params.get(), get(), get(), get())
    }

    viewModel { SettingsViewModel(get(), get()) }

    viewModel { FavoritesViewModel(get()) }

    viewModel { PlaylistsViewModel(get()) }

    viewModel { PlaylistCreateViewModel(get()) }

    viewModel { PlaylistInfoViewModel(get(), get()) }

}