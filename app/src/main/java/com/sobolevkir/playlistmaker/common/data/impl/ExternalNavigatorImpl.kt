package com.sobolevkir.playlistmaker.common.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sobolevkir.playlistmaker.common.domain.ExternalNavigator

class ExternalNavigatorImpl(private val appContext: Context) : ExternalNavigator {
    override fun shareText(text: String) {
        Intent(Intent.ACTION_SEND).run {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
            startIntent(this)
        }
    }

    override fun openLink(url: String) {
        Intent(Intent.ACTION_VIEW).run {
            data = Uri.parse(url)
            startIntent(this)
        }
    }

    override fun writeEmail(subject: String, message: String, email: String) {
        Intent(Intent.ACTION_SENDTO).run {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            startIntent(this)
        }
    }

    private fun startIntent(intent: Intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
    }
}