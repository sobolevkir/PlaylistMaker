package com.sobolevkir.playlistmaker.common.domain

interface LocalStorage {
    fun read(key: String, defValue: Boolean): Boolean
    fun write(key: String, value: Boolean)

    fun read(key: String, defValue: String): String
    fun write(key: String, value: String)

    fun contains(key: String): Boolean

    companion object {
        const val PLAYLISTMAKER_PARAMS = "local_storage"
    }
}