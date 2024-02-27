package com.sobolevkir.playlistmaker.settings.ui.viewmodel

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sobolevkir.playlistmaker.App
import com.sobolevkir.playlistmaker.creator.Creator
import com.sobolevkir.playlistmaker.settings.domain.SettingsInteractor
import com.sobolevkir.playlistmaker.settings.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val app: App
) : ViewModel(), DefaultLifecycleObserver {
    private val darkThemeLiveData = MutableLiveData<Boolean>()

    fun getDarkThemeLiveData(): LiveData<Boolean> = darkThemeLiveData

    fun onThemeSwitcherCheckChange(isDarkThemeOn: Boolean) {
        Log.d("vm-settings", "onThemeSwitcherCheckChange()")
        app.switchTheme(isDarkThemeOn)
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
                app.isSystemThemeDark()
            }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    Creator.provideSharingInteractor(),
                    Creator.provideSettingsInteractor(),
                    (this[APPLICATION_KEY] as App)
                )
            }
        }
    }
}