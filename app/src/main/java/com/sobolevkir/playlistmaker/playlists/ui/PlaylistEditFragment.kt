package com.sobolevkir.playlistmaker.playlists.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import com.sobolevkir.playlistmaker.playlists.presentation.PlaylistEditViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditFragment : PlaylistCreateFragment() {

    private val args: PlaylistEditFragmentArgs by navArgs()
    override val viewModel: PlaylistEditViewModel by viewModel { parametersOf(args.currentPlaylistId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { setPlaylistInfo(it) }
        onBackPressedCallback.remove()
        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            toolbar.title = getString(R.string.title_edit)
            btnSubmit.text = getString(R.string.btn_save)
            btnSubmit.setOnClickListener {
                viewModel.onSubmitButtonClick(
                    name = etPlaylistName.text.toString(),
                    description = etPlaylistDescription.text.toString(),
                    strCoverUri = strCoverUri
                )
                findNavController().popBackStack()
                showResultMessage(
                    getString(
                        R.string.message_success_editing_playlist,
                        etPlaylistName.text.toString()
                    )
                )
            }
        }
    }

    private fun setPlaylistInfo(playlist: Playlist) {
        super.showCover(playlist.coverUri)
        strCoverUri = playlist.coverUri
        binding.etPlaylistName.setText(playlist.name)
        binding.etPlaylistDescription.setText(playlist.description)
    }

}