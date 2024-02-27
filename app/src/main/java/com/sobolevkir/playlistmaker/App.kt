package com.sobolevkir.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.sobolevkir.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.setApplication(this)
        applyThemeSettings()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyThemeSettings()
    }

    private fun applyThemeSettings() {
        val settingsInteractor = Creator.provideSettingsInteractor()
        switchTheme(
            if (settingsInteractor.isThemeDataCreated()) {
                settingsInteractor.isDataThemeDark()
            } else {
                isSystemThemeDark()
            }
        )
    }

    fun isSystemThemeDark(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}