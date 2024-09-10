package com.sobolevkir.playlistmaker.common.domain

interface ResourceProvider {

    fun getString(resId: Int): String

    fun getQuantityString(resId: Int, number: Int): String

}