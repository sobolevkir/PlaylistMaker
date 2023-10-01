package com.sobolevkir.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sobolevkir.playlistmaker.PrefsManager.IS_DARK_THEME_ON

class App: Application() {

    private var isDarkThemeOn = false

    override fun onCreate() {
        super.onCreate()
        PrefsManager.init(this.applicationContext)
        isDarkThemeOn = PrefsManager.read(IS_DARK_THEME_ON, false)
        switchTheme(isDarkThemeOn)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkThemeOn = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

}