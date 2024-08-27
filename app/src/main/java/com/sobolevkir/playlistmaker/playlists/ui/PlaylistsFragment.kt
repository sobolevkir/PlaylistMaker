package com.sobolevkir.playlistmaker.playlists.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.util.viewBinding
import com.sobolevkir.playlistmaker.databinding.FragmentPlaylistsBinding
import com.sobolevkir.playlistmaker.media.ui.MediaFragmentDirections
import com.sobolevkir.playlistmaker.playlists.presentation.PlaylistsViewModel
import com.sobolevkir.playlistmaker.playlists.presentation.PlaylistsState
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

        binding.btnCreatePlaylist.setOnClickListener {
            val action = MediaFragmentDirections.actionMediaFragmentToNewPlaylistFragment()
            findNavController().navigate(action)
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