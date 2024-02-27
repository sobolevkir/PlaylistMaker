package com.sobolevkir.playlistmaker.settings.data.impl

import com.sobolevkir.playlistmaker.common.domain.LocalStorage
import com.sobolevkir.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val localStorage: LocalStorage) : SettingsRepository {
    override fun isThemeDataCreated(): Boolean {
        return localStorage.contains(IS_DARK_THEME_ON)
    }

    override fun isDataThemeDark(): Boolean {
        return localStorage.read(IS_DARK_THEME_ON, false)
    }

    override fun saveThemeData(isDarkThemeOn: Boolean) {
        localStorage.write(IS_DARK_THEME_ON, isDarkThemeOn)
    }

    companion object {
        const val IS_DARK_THEME_ON = "is_dark_theme"
    }
}