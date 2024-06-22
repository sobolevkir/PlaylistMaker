package com.sobolevkir.playlistmaker.media.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.util.viewBinding
import com.sobolevkir.playlistmaker.databinding.FragmentPlaylistsBinding
import com.sobolevkir.playlistmaker.media.presentation.PlaylistsViewModel
import com.sobolevkir.playlistmaker.media.presentation.model.PlaylistsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private val viewModel: PlaylistsViewModel by viewModel()
    private val binding by viewBinding(FragmentPlaylistsBinding::bind)

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