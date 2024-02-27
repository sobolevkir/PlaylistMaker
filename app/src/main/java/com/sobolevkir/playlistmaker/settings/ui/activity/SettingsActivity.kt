package com.sobolevkir.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sobolevkir.playlistmaker.databinding.ActivitySettingsBinding
import com.sobolevkir.playlistmaker.settings.ui.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel> { SettingsViewModel.getViewModelFactory() }
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycle.addObserver(viewModel)

        viewModel.getDarkThemeLiveData().observe(this) {
            binding.swThemeSwitcher.isChecked = it
        }
        with(binding) {
            btnBack.setOnClickListener { finish() }
            swThemeSwitcher.setOnCheckedChangeListener { switch, isChecked ->
                if (switch.isPressed) viewModel.onThemeSwitcherCheckChange(isChecked)
            }
            btnShareApp.setOnClickListener {
                viewModel.shareApp()
            }
            btnSupport.setOnClickListener {
                viewModel.contactSupport()
            }
            btnUserAgreement.setOnClickListener {
                viewModel.openUserAgreement()
            }
        }
    }
}