package com.sobolevkir.playlistmaker.presentation.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.app.App
import com.sobolevkir.playlistmaker.databinding.ActivitySettingsBinding
import com.sobolevkir.playlistmaker.domain.api.LocalStorage.Companion.IS_DARK_THEME_ON
import com.sobolevkir.playlistmaker.util.Creator

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val localStorage = Creator.provideLocalStorage()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.run {

            btnBack.setOnClickListener { finish() }

            with(swThemeSwitcher) {
                isChecked = if (localStorage.isThemeDataCreated()) {
                    localStorage.read(IS_DARK_THEME_ON, false)
                } else {
                    (applicationContext as App).isSystemThemeDark()
                }
                setOnCheckedChangeListener { _, checked ->
                    (applicationContext as App).switchTheme(checked)
                    localStorage.write(IS_DARK_THEME_ON, checked)
                }
            }

            btnShareApp.setOnClickListener {
                Intent(Intent.ACTION_SEND).run {
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.url_share_app))
                    type = "text/plain"
                    startActivity(this)
                }
            }

            btnSupport.setOnClickListener {
                Intent(Intent.ACTION_SENDTO).run {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_mail_address)))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail_subject))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.support_mail_message))
                    startActivity(this)
                }
            }

            btnUserAgreement.setOnClickListener {
                Intent(Intent.ACTION_VIEW).run {
                    data = Uri.parse(getString(R.string.url_user_agreement))
                    startActivity(this)
                }
            }

        }
    }
}