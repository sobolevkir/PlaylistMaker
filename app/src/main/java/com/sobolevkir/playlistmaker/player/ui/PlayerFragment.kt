package com.sobolevkir.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.msaggik.playlistmaker.media.presentation.ui.adapters.PlaylistSmallListAdapter
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.databinding.FragmentPlayerBinding
import com.sobolevkir.playlistmaker.player.presentation.PlayerState
import com.sobolevkir.playlistmaker.player.presentation.PlayerViewModel
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import com.sobolevkir.playlistmaker.util.debounce
import com.sobolevkir.playlistmaker.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val binding by viewBinding(FragmentPlayerBinding::bind)

    private val args: PlayerFragmentArgs by navArgs()
    private val viewModel: PlayerViewModel by viewModel { parametersOf(args.currentTrackInfo) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit
    private var playlistsAdapter: PlaylistSmallListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickDebounce()
        initAdapters()
        initObservers()
        initListeners()
        initBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playlistsAdapter = null
        binding.rvPlaylistsList.adapter = null
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObservers() {
        lifecycle.addObserver(viewModel)
        viewModel.getCurrentTrackLiveData().observe(viewLifecycleOwner) { setTrackInfo(it) }
        viewModel.getPlayerStateLiveData().observe(viewLifecycleOwner) { playerState ->
            changePlayControlButton(playerState)
            when (playerState) {
                is PlayerState.Playing -> binding.tvCurrentPosition.text = playerState.progress
                is PlayerState.Paused -> binding.tvCurrentPosition.text = playerState.progress
                else -> binding.tvCurrentPosition.text = getString(R.string.hint_track_duration)
            }
        }
        viewModel.getPlaylistsLiveData().observe(viewLifecycleOwner) { playlists ->
            playlistsAdapter?.playlists?.clear()
            playlistsAdapter?.playlists?.addAll(playlists)
            playlistsAdapter?.notifyDataSetChanged()
        }
        viewModel.getAddingResultSingleLiveEvent().observe(viewLifecycleOwner) { (isAddingSuccess, playlistName) ->
            if (isAddingSuccess) {
                showMessage(getString(R.string.message_success_track_adding, playlistName))
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                showMessage(getString(R.string.message_duplicate_track, playlistName))
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            btnPlayControl.setOnClickListener { viewModel.playbackControl() }
            btnFavorite.setOnClickListener { viewModel.onFavoriteButtonClick() }
            btnAddToPlaylist.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            btnCreatePlaylist.setOnClickListener { openPlaylistCreation() }
        }
    }

    private fun initAdapters() {
        playlistsAdapter = PlaylistSmallListAdapter { onPlaylistClickDebounce(it) }
        binding.rvPlaylistsList.adapter = playlistsAdapter
    }

    private fun initClickDebounce() {
        onPlaylistClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false
        ) { playlist -> viewModel.onPlaylistSelect(playlist) }
    }

    private fun setTrackInfo(track: Track) {
        with(binding) {
            btnFavorite.setImageDrawable(getFavoriteButtonDrawable(track.isFavorite))
            Glide.with(this@PlayerFragment).load(track.artworkUrl512).centerInside()
                .placeholder(R.drawable.cover_placeholder).into(ivAlbumCoverLarge)
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            if (track.formattedTrackTime.isNotEmpty()) {
                tvDurationValue.text = track.formattedTrackTime
            } else {
                tvDurationValue.isVisible = false
                tvDurationTitle.isVisible = false
            }
            if (track.collectionName.isNotEmpty()) {
                tvCollectionNameValue.text = track.collectionName
            } else {
                tvCollectionNameValue.isVisible = false
                tvCollectionNameTitle.isVisible = false
            }
            if (track.releaseYear.isNotEmpty()) {
                tvReleaseYearValue.text = track.releaseYear
            } else {
                tvReleaseYearValue.isVisible = false
                tvReleaseYearTitle.isVisible = false
            }
            if (track.primaryGenreName.isNotEmpty()) {
                tvGenreValue.text = track.primaryGenreName
            } else {
                tvGenreValue.isVisible = false
                tvGenreTitle.isVisible = false
            }
            if (track.country.isNotEmpty()) {
                tvCountryValue.text = track.country
            } else {
                tvCountryValue.isVisible = false
                tvCountryTitle.isVisible = false
            }
        }
    }

    private fun changePlayControlButton(playerState: PlayerState) {
        binding.btnPlayControl.run {
            when (playerState) {
                is PlayerState.Prepared, is PlayerState.Paused -> {
                    this.alpha = 1.0f
                    this.isEnabled = true
                    this.setImageResource(R.drawable.btn_play_icon)
                }

                is PlayerState.Playing -> this.setImageResource(R.drawable.btn_pause_icon)
                else -> {
                    this.alpha = 0.25f
                    this.isEnabled = false
                }
            }
        }
    }

    private fun getFavoriteButtonDrawable(inFavorite: Boolean): Drawable? {
        return ContextCompat.getDrawable(
            requireContext(),
            if (inFavorite) R.drawable.btn_favorite_active_icon else R.drawable.btn_favorite_inactive_icon
        )
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + 1
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun openPlaylistCreation() {
        val action = PlayerFragmentDirections.actionPlayerFragmentToCreatePlaylistFragment()
        findNavController().navigate(action)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
    }
}