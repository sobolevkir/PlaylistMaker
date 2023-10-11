package com.sobolevkir.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sobolevkir.playlistmaker.PrefsManager.IS_DARK_THEME_ON

class App: Application() {

    private var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()
        PrefsManager.init(this.applicationContext)
        isDarkTheme = PrefsManager.read(IS_DARK_THEME_ON, false)
        switchTheme(isDarkTheme)
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

}