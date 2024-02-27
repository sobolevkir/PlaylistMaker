package com.sobolevkir.playlistmaker.settings.domain

interface SettingsInteractor {
    fun isThemeDataCreated(): Boolean
    fun isDataThemeDark(): Boolean
    fun saveThemeData(isDarkThemeOn: Boolean)
}