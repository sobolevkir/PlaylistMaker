package com.sobolevkir.playlistmaker.settings.domain

interface SettingsInteractor {
    fun applyThemeSettings()
    fun isThemeDataCreated(): Boolean
    fun isDataThemeDark(): Boolean
    fun saveThemeData(isDarkThemeOn: Boolean)
    fun isSystemThemeDark(): Boolean
    fun switchTheme(darkThemeEnabled: Boolean)
}