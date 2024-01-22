package com.sobolevkir.playlistmaker.app

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.sobolevkir.playlistmaker.domain.api.LocalStorage.Companion.IS_DARK_THEME_ON
import com.sobolevkir.playlistmaker.util.Creator

class App : Application() {

    private var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()

        Creator.setApplication(this)
        val localStorage = Creator.provideLocalStorage()

        if (localStorage.isThemeDataCreated()) {
            isDarkTheme = localStorage.read(IS_DARK_THEME_ON, false)
            switchTheme(isDarkTheme)
        } else {
            if (isSystemThemeDark()) {
                switchTheme(true)
            }
        }

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isSystemThemeDark(): Boolean {
        return (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

}