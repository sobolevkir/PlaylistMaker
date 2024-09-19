package com.sobolevkir.playlistmaker.favorites.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.ui.TrackListAdapter
import com.sobolevkir.playlistmaker.common.util.debounce
import com.sobolevkir.playlistmaker.common.util.viewBinding
import com.sobolevkir.playlistmaker.databinding.FragmentFavoritesBinding
import com.sobolevkir.playlistmaker.favorites.presentation.FavoritesState
import com.sobolevkir.playlistmaker.favorites.presentation.FavoritesViewModel
import com.sobolevkir.playlistmaker.media.ui.MediaFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel: FavoritesViewModel by viewModel()
    private val binding by viewBinding(FragmentFavoritesBinding::bind)
    private var favoritesAdapter: TrackListAdapter? = null
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickDebounce()
        initAdapters()
        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesAdapter = null
        binding.rvFavoritesList.adapter = null
    }

    private fun initClickDebounce() {
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track -> openPlayer(track) }
    }

    private fun initAdapters() {
        favoritesAdapter = TrackListAdapter(onItemClick = { onTrackClickDebounce(it) })
        binding.rvFavoritesList.adapter = favoritesAdapter
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Loading -> showLoading()
            is FavoritesState.Empty -> showNothingFound()
            is FavoritesState.Content -> showContent(state.tracks)
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            tvNothingFound.isVisible = false
            rvFavoritesList.isVisible = false
        }
    }

    private fun showNothingFound() {
        binding.apply {
            progressBar.isVisible = false
            rvFavoritesList.isVisible = false
            tvNothingFound.isVisible = true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<Track>) {
        binding.apply {
            progressBar.isVisible = false
            rvFavoritesList.isVisible = true
            tvNothingFound.isVisible = false
        }
        favoritesAdapter?.apply {
            this.tracks.clear()
            this.tracks.addAll(tracks)
            notifyDataSetChanged()
        }
    }

    private fun openPlayer(track: Track) {
        val action = MediaFragmentDirections.actionMediaFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
    }

}