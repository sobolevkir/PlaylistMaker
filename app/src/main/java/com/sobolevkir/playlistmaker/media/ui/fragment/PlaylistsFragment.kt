package com.sobolevkir.playlistmaker.media.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.sobolevkir.playlistmaker.common.ui.BindingFragment
import com.sobolevkir.playlistmaker.databinding.FragmentPlaylistsBinding
import com.sobolevkir.playlistmaker.media.ui.model.PlaylistsState
import com.sobolevkir.playlistmaker.media.ui.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : BindingFragment<FragmentPlaylistsBinding>() {

    private val viewModel: PlaylistsViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistsBinding {
        return FragmentPlaylistsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Content -> showNothingFound()
                is PlaylistsState.NothingFound -> showNothingFound()
            }
        }
    }

    private fun showNothingFound() {
        binding.apply {
            tvNothingFound.isVisible = true
            btnCreatePlaylist.isVisible = true
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}