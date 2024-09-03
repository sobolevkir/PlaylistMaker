package com.sobolevkir.playlistmaker.common.data.impl

import android.content.SharedPreferences
import com.sobolevkir.playlistmaker.common.domain.LocalStorage

class LocalStorageImpl(private val prefs: SharedPreferences) : LocalStorage {

    override fun read(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }
    override fun write(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun read(key: String, defValue: String): String {
        return prefs.getString(key, defValue) ?: ""
    }
    override fun write(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun contains(key: String): Boolean {
        return prefs.contains(key)
    }
}
