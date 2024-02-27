package com.sobolevkir.playlistmaker.settings.domain

interface SettingsRepository {
    fun isThemeDataCreated(): Boolean
    fun isDataThemeDark(): Boolean
    fun saveThemeData(isDarkThemeOn: Boolean)
}