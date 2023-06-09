package com.sobolevkir.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.button_back)
        val shareButton = findViewById<Button>(R.id.button_share)
        val supportButton = findViewById<Button>(R.id.button_support)
        val userAgreementButton = findViewById<Button>(R.id.button_user_agreement)

        backButton.setOnClickListener{ finish() }

        shareButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                type = "text/plain"
                startActivity(this)
            }
        }

        supportButton.setOnClickListener{
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_mail_message))
                startActivity(this)
            }
        }

        userAgreementButton.setOnClickListener{
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.user_agreement_link))
                startActivity(this)
            }
        }

    }
}