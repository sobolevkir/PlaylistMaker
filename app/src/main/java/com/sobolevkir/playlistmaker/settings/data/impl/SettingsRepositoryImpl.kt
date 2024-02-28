package com.sobolevkir.playlistmaker.settings.data.impl

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.sobolevkir.playlistmaker.common.domain.LocalStorage
import com.sobolevkir.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val localStorage: LocalStorage, private val context: Context) :
    SettingsRepository {

    override fun applyThemeSettings() {
        switchTheme(
            if (isThemeDataCreated()) {
                isDataThemeDark()
            } else {
                isSystemThemeDark()
            }
        )
    }

    override fun isThemeDataCreated(): Boolean {
        return localStorage.contains(IS_DARK_THEME_ON)
    }

    override fun isDataThemeDark(): Boolean {
        return localStorage.read(IS_DARK_THEME_ON, false)
    }

    override fun saveThemeData(isDarkThemeOn: Boolean) {
        localStorage.write(IS_DARK_THEME_ON, isDarkThemeOn)
    }

    override fun isSystemThemeDark(): Boolean {
        return (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val IS_DARK_THEME_ON = "is_dark_theme"
    }
}