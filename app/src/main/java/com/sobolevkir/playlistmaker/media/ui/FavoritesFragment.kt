package com.sobolevkir.playlistmaker.media.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.ext.viewBinding
import com.sobolevkir.playlistmaker.databinding.FragmentFavoritesBinding
import com.sobolevkir.playlistmaker.media.presentation.FavoritesViewModel
import com.sobolevkir.playlistmaker.media.presentation.model.FavoritesState
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel: FavoritesViewModel by viewModel()
    private val binding by viewBinding(FragmentFavoritesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoritesState.Content -> showNothingFound()
                is FavoritesState.NothingFound -> showNothingFound()
            }
        }
    }

    private fun showNothingFound() {
        binding.apply {
            tvNothingFound.isVisible = true
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

}