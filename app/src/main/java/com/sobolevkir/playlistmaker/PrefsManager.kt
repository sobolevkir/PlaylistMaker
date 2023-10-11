package com.sobolevkir.playlistmaker

import android.content.Context
import android.content.SharedPreferences

object PrefsManager {
    private lateinit var prefs: SharedPreferences
    private const val PREFS_NAME = "playlist_maker_params"
    const val IS_DARK_THEME_ON = "is_dark_theme"
    const val TRACK_HISTORY_LIST = "track_history_list"

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun read(key: String, value: Boolean): Boolean {
        return prefs.getBoolean(key, value)
    }
    fun write(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun read(key: String, value: String?): String? {
        return prefs.getString(key, value)
    }
    fun write(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

}