package com.sobolevkir.playlistmaker.common.domain

interface ExternalNavigator {
    fun shareText(text: String)
    fun openLink(url: String)
    fun writeEmail(subject: String, message: String, email: String)
}