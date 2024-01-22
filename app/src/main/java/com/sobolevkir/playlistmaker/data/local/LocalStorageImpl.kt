package com.sobolevkir.playlistmaker.data.local

import android.content.SharedPreferences
import com.sobolevkir.playlistmaker.domain.api.LocalStorage
import com.sobolevkir.playlistmaker.domain.api.LocalStorage.Companion.IS_DARK_THEME_ON

class LocalStorageImpl(private val prefs: SharedPreferences): LocalStorage {

    override fun isThemeDataCreated(): Boolean {
        return prefs.contains(IS_DARK_THEME_ON)
    }

    override fun read(key: String, value: Boolean): Boolean {
        return prefs.getBoolean(key, value)
    }

    override fun write(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun read(key: String, value: String?): String? {
        return prefs.getString(key, value)
    }

    override fun write(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

}
