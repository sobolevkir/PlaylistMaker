package com.sobolevkir.playlistmaker.settings.domain.impl

import com.sobolevkir.playlistmaker.settings.domain.SettingsInteractor
import com.sobolevkir.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun applyThemeSettings() {
        repository.applyThemeSettings()
    }

    override fun isThemeDataCreated(): Boolean {
        return repository.isThemeDataCreated()
    }

    override fun isDataThemeDark(): Boolean {
        return repository.isDataThemeDark()
    }

    override fun saveThemeData(isDarkThemeOn: Boolean) {
        repository.saveThemeData(isDarkThemeOn)
    }

    override fun isSystemThemeDark(): Boolean {
        return repository.isSystemThemeDark()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }


}