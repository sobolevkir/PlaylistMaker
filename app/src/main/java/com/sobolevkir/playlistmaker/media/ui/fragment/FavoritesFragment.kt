package com.sobolevkir.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.sobolevkir.playlistmaker.common.ui.BindingFragment
import com.sobolevkir.playlistmaker.databinding.FragmentFavoritesBinding
import com.sobolevkir.playlistmaker.media.ui.model.FavoritesState
import com.sobolevkir.playlistmaker.media.ui.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

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