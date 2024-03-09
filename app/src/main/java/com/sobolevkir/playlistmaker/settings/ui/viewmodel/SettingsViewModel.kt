package com.sobolevkir.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobolevkir.playlistmaker.settings.domain.SettingsInteractor
import com.sobolevkir.playlistmaker.settings.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel(), DefaultLifecycleObserver {
    private val darkThemeLiveData = MutableLiveData<Boolean>()

    fun getDarkThemeLiveData(): LiveData<Boolean> = darkThemeLiveData

    fun onThemeSwitcherCheckChange(isDarkThemeOn: Boolean) {
        settingsInteractor.switchTheme(isDarkThemeOn)
        settingsInteractor.saveThemeData(isDarkThemeOn)
        darkThemeLiveData.value = isDarkThemeOn
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun contactSupport() {
        sharingInteractor.contactSupport()
    }

    fun openUserAgreement() {
        sharingInteractor.openUserAgreement()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        darkThemeLiveData.value =
            if (settingsInteractor.isThemeDataCreated()) {
                settingsInteractor.isDataThemeDark()
            } else {
                settingsInteractor.isSystemThemeDark()
            }
    }
}