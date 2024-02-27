package com.sobolevkir.playlistmaker.common.domain

interface ResourceProvider {
    fun getString(resId: Int): String
}