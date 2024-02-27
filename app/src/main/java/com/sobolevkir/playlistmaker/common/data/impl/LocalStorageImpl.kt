package com.sobolevkir.playlistmaker.common.data.impl

import android.content.SharedPreferences
import com.sobolevkir.playlistmaker.common.domain.LocalStorage

class LocalStorageImpl(private val prefs: SharedPreferences) : LocalStorage {

    override fun read(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }
    override fun write(key: String, defValue: Boolean) {
        prefs.edit().putBoolean(key, defValue).apply()
    }

    override fun read(key: String, defValue: String?): String? {
        return prefs.getString(key, defValue)
    }
    override fun write(key: String, defValue: String?) {
        prefs.edit().putString(key, defValue).apply()
    }

    override fun read(key: String, defValue: Set<String>): Set<String> {
        return prefs.getStringSet(key, defValue) ?: emptySet()
    }
    override fun write(key: String, defValue: Set<String>) {
        prefs.edit().putStringSet(key, defValue).apply()
    }

    override fun contains(key: String): Boolean {
        return prefs.contains(key)
    }
}
