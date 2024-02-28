package com.sobolevkir.playlistmaker

import android.app.Application
import android.content.res.Configuration
import com.sobolevkir.playlistmaker.creator.Creator
import com.sobolevkir.playlistmaker.settings.domain.SettingsInteractor

class App : Application() {

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.setApplication(this)
        settingsInteractor = Creator.provideSettingsInteractor()
        settingsInteractor.applyThemeSettings()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        settingsInteractor.applyThemeSettings()
    }

}