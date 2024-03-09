package com.sobolevkir.playlistmaker.di

import com.sobolevkir.playlistmaker.player.ui.viewmodel.PlayerViewModel
import com.sobolevkir.playlistmaker.search.ui.viewmodel.SearchViewModel
import com.sobolevkir.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SearchViewModel(get()) }
    viewModel {params ->
        PlayerViewModel(params.get(), get(), get())
    }
    viewModel { SettingsViewModel(get(), get()) }
}