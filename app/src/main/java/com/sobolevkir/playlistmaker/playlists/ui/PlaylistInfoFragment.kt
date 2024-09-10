package com.sobolevkir.playlistmaker.playlists.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.ui.TrackListAdapter
import com.sobolevkir.playlistmaker.common.util.debounce
import com.sobolevkir.playlistmaker.common.util.viewBinding
import com.sobolevkir.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.sobolevkir.playlistmaker.playlists.domain.model.PlaylistWithTracks
import com.sobolevkir.playlistmaker.playlists.presentation.PlaylistInfoViewModel
import com.sobolevkir.playlistmaker.playlists.presentation.model.PlaylistInfoEvent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment : Fragment(R.layout.fragment_playlist_info) {

    private val binding by viewBinding(FragmentPlaylistInfoBinding::bind)
    private val args: PlaylistInfoFragmentArgs by navArgs()
    private val viewModel: PlaylistInfoViewModel by viewModel { parametersOf(args.currentPlaylistId) }
    private lateinit var tracksBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
    private var tracksAdapter: TrackListAdapter? = null
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
        initAdapters()
        initClickDebounce()
        initBottomSheets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tracksAdapter = null
        binding.rvTracksList.adapter = null
        menuBottomSheetBehavior.removeBottomSheetCallback(menuBottomSheetCallback)
    }

    private fun initListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            btnMenu.setOnClickListener {
                menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            btnSharePlaylistSmall.setOnClickListener { viewModel.sharePlaylist() }
            btnSharePlaylist.setOnClickListener { viewModel.sharePlaylist() }
            btnEditPlaylistInfo.setOnClickListener {
                val playlistId = viewModel.getPlaylistWithTracksLiveData().value?.id ?: -1
                val action = PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToPlaylistEditFragment(playlistId)
                findNavController().navigate(action)
            }
            btnRemovePlaylist.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog).apply {
                    setTitle(R.string.warning_delete_playlist)
                    setNeutralButton(R.string.action_no) { _, _ -> }
                    setPositiveButton(R.string.action_yes) { _, _ ->
                        viewModel.removePlaylist()
                    }
                    show()
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.getPlaylistWithTracksLiveData()
            .observe(viewLifecycleOwner) { playlistWithTracks ->
                setPlaylistData(playlistWithTracks)
            }
        viewModel.getResultSingleLiveEvent()
            .observe(viewLifecycleOwner) { playlistInfoEvent ->
                when (playlistInfoEvent) {
                    is PlaylistInfoEvent.TrackRemovedSuccess -> showMessage(
                        getString(
                            R.string.message_success_track_removed,
                            playlistInfoEvent.trackName
                        )
                    )

                    is PlaylistInfoEvent.PlaylistEditedSuccess -> showMessage(
                        getString(
                            R.string.message_success_track_removed,
                            playlistInfoEvent.playlistName
                        )
                    )

                    is PlaylistInfoEvent.PlaylistRemovedSuccess -> {
                        showMessage(
                            getString(
                                R.string.message_success_playlist_removed,
                                playlistInfoEvent.playlistName
                            )
                        )
                        findNavController().popBackStack()
                    }

                    is PlaylistInfoEvent.UnsuccessfulSharing -> showMessage(
                        getString(R.string.message_unsuccessful_sharing)
                    )
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPlaylistData(playlistWithTracks: PlaylistWithTracks) {
        with(binding) {
            Glide.with(this@PlaylistInfoFragment)
                .load(playlistWithTracks.coverUri.toUri())
                .centerCrop().placeholder(R.drawable.cover_placeholder).into(ivPlaylistCoverLarge)

            tvPlaylistName.text = playlistWithTracks.name
            tvPlaylistDuration.text = root.context.resources.getQuantityString(
                R.plurals.playlist_duration,
                playlistWithTracks.totalMinutesDuration,
                playlistWithTracks.totalMinutesDuration
            )

            tvPlaylistDescription.apply {
                text = playlistWithTracks.description
                isVisible = playlistWithTracks.description.isNotEmpty()
            }

            tvPlaylistTracksNumber.text = root.context.resources.getQuantityString(
                R.plurals.tracks_count,
                playlistWithTracks.tracksNumber,
                playlistWithTracks.tracksNumber
            )

            if (playlistWithTracks.tracks.isEmpty()) {
                rvTracksList.isVisible = false
                tvEmptyPlaylistMessage.isVisible = true
            } else {
                tracksAdapter?.apply {
                    tracks.clear()
                    tracks.addAll(playlistWithTracks.tracks)
                    notifyDataSetChanged()
                }
                rvTracksList.isVisible = true
                tvEmptyPlaylistMessage.isVisible = false
            }

            includedPlaylistItem.tvPlaylistName.text = playlistWithTracks.name
            includedPlaylistItem.tvTracksNumber.text = tvPlaylistTracksNumber.text
            val cornerRadius = binding.root.resources.getDimensionPixelSize(R.dimen.radius_small)
            Glide.with(this@PlaylistInfoFragment)
                .load(playlistWithTracks.coverUri.toUri())
                .transform(CenterCrop(), RoundedCorners(cornerRadius))
                .placeholder(R.drawable.cover_placeholder)
                .into(includedPlaylistItem.ivCoverPlaylist)
        }
    }

    private fun initBottomSheets() {
        menuBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset + ENABLED_ALPHA
            }
        }
        tracksBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            peekHeight =
                (resources.displayMetrics.heightPixels * TRACKS_BOTTOM_SHEET_HEIGHT_WEIGHT).toInt()
        }
        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight =
                (resources.displayMetrics.heightPixels * MENU_BOTTOM_SHEET_HEIGHT_WEIGHT).toInt()
            addBottomSheetCallback(menuBottomSheetCallback)
        }
        with(binding) {
            ivPlaylistCoverLarge.maxHeight = resources.displayMetrics.widthPixels
            main.layoutParams.height =
                (resources.displayMetrics.heightPixels * MAIN_HEIGHT_WEIGHT).toInt()
        }
    }

    private fun initAdapters() {
        tracksAdapter = TrackListAdapter(onItemClick = { onTrackClickDebounce(it) },
            onItemLongClick = { track ->
                MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog).apply {
                    setTitle(R.string.warning_delete_track)
                    setNeutralButton(R.string.action_no) { _, _ -> }
                    setPositiveButton(R.string.action_yes) { _, _ ->
                        viewModel.removeTrackFromPlaylist(track)
                    }
                    show()
                }
            })
        binding.rvTracksList.adapter = tracksAdapter
    }

    private fun initClickDebounce() {
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS, viewLifecycleOwner.lifecycleScope, false
        ) { track -> openPlayer(track) }
    }

    private fun openPlayer(track: Track) {
        val action =
            PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 100L
        private const val ENABLED_ALPHA = 1.0f
        private const val TRACKS_BOTTOM_SHEET_HEIGHT_WEIGHT = 0.3
        private const val MENU_BOTTOM_SHEET_HEIGHT_WEIGHT = 0.45
        private const val MAIN_HEIGHT_WEIGHT = 0.7
    }

}