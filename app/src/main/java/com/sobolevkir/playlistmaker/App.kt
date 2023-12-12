package com.sobolevkir.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sobolevkir.playlistmaker.PrefsManager.IS_DARK_THEME_ON

class App: Application() {

    private var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()
        PrefsManager.init(this.applicationContext)
        if(PrefsManager.isThemeDataCreated()) {
            isDarkTheme = PrefsManager.read(IS_DARK_THEME_ON, false)
            switchTheme(isDarkTheme)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
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

}