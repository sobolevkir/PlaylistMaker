package com.sobolevkir.playlistmaker.settings.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.ext.viewBinding
import com.sobolevkir.playlistmaker.databinding.FragmentSettingsBinding
import com.sobolevkir.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel by viewModel<SettingsViewModel>()
    private val binding by viewBinding(FragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        lifecycle.addObserver(viewModel)
        viewModel.getDarkThemeLiveData().observe(viewLifecycleOwner) {
            binding.swThemeSwitcher.isChecked = it
        }
    }

    private fun initListeners() {
        with(binding) {
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