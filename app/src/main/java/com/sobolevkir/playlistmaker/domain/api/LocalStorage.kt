package com.sobolevkir.playlistmaker.domain.api

interface LocalStorage {

    companion object {
        const val IS_DARK_THEME_ON = "is_dark_theme"
        const val PREFS_NAME = "playlist_maker_params"
    }

    fun isThemeDataCreated(): Boolean

    fun read(key: String, value: Boolean): Boolean

    fun write(key: String, value: Boolean)

    fun read(key: String, value: String?): String?

    fun write(key: String, value: String?)

}