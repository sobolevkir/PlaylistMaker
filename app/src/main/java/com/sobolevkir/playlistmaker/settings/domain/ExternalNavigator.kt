package com.sobolevkir.playlistmaker.settings.domain

interface ExternalNavigator {
    fun shareLink(url: String)
    fun openLink(url: String)
    fun writeEmail(subject: String, message: String, email: String)
}