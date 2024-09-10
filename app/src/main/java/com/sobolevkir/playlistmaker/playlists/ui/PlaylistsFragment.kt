package com.sobolevkir.playlistmaker.playlists.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.msaggik.playlistmaker.media.presentation.ui.adapters.PlaylistLargeListAdapter
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.databinding.FragmentPlaylistsBinding
import com.sobolevkir.playlistmaker.media.ui.MediaFragmentDirections
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import com.sobolevkir.playlistmaker.playlists.presentation.model.PlaylistsState
import com.sobolevkir.playlistmaker.playlists.presentation.PlaylistsViewModel
import com.sobolevkir.playlistmaker.common.util.debounce
import com.sobolevkir.playlistmaker.common.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private val viewModel: PlaylistsViewModel by viewModel()
    private val binding by viewBinding(FragmentPlaylistsBinding::bind)
    private var playlistsAdapter: PlaylistLargeListAdapter? = null
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickDebounce()
        initAdapters()
        viewModel.fillData()
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        binding.btnCreatePlaylist.setOnClickListener {
            val action = MediaFragmentDirections.actionMediaFragmentToPlaylistCreateFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playlistsAdapter = null
        binding.rvPlaylistsList.adapter = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun initClickDebounce() {
        onPlaylistClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist -> openPlaylist(playlist.id) }
    }

    private fun initAdapters() {
        playlistsAdapter = PlaylistLargeListAdapter { onPlaylistClickDebounce(it) }
        binding.rvPlaylistsList.adapter = playlistsAdapter
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Loading -> showLoading()
            is PlaylistsState.Content -> showContent(state.playlists)
            is PlaylistsState.NothingFound -> showNothingFound()
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            tvNothingFound.isVisible = false
            rvPlaylistsList.isVisible = false
            btnCreatePlaylist.isVisible = false
        }
    }

    private fun showNothingFound() {
        binding.apply {
            progressBar.isVisible = false
            tvNothingFound.isVisible = true
            rvPlaylistsList.isVisible = false
            btnCreatePlaylist.isVisible = true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(playlists: List<Playlist>) {
        binding.apply {
            progressBar.isVisible = false
            rvPlaylistsList.isVisible = true
            tvNothingFound.isVisible = false
            btnCreatePlaylist.isVisible = true
        }
        playlistsAdapter?.apply {
            this.playlists.clear()
            this.playlists.addAll(playlists)
            notifyDataSetChanged()
        }
    }

    private fun openPlaylist(playlistId: Long) {
        val action = MediaFragmentDirections.actionMediaFragmentToPlaylistInfoFragment(playlistId)
        findNavController().navigate(action)
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }
}