package com.sobolevkir.playlistmaker.playlists.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.util.viewBinding
import com.sobolevkir.playlistmaker.databinding.FragmentNewPlaylistBinding

class NewPlaylistFragment : Fragment(R.layout.fragment_new_playlist) {

    //private val viewModel: NewPlaylistViewModel by viewModel()
    private val binding by viewBinding(FragmentNewPlaylistBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        /*viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Content -> showNothingFound()
                is PlaylistsState.NothingFound -> showNothingFound()
            }
        }*/
    }

}