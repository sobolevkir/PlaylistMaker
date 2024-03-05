package com.sobolevkir.playlistmaker

import android.app.Application
import android.content.res.Configuration
import com.sobolevkir.playlistmaker.di.dataModule
import com.sobolevkir.playlistmaker.di.interactorModule
import com.sobolevkir.playlistmaker.di.repositoryModule
import com.sobolevkir.playlistmaker.di.viewModelModule
import com.sobolevkir.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class App : Application(), KoinComponent {

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        settingsInteractor = getKoin().get()
        settingsInteractor.applyThemeSettings()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        settingsInteractor.applyThemeSettings()
    }

}