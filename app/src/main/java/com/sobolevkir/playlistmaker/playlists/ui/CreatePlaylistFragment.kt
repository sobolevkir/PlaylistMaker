package com.sobolevkir.playlistmaker.playlists.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.sobolevkir.playlistmaker.playlists.presentation.CreatePlaylistViewModel
import com.sobolevkir.playlistmaker.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment(R.layout.fragment_create_playlist) {

    private val binding by viewBinding(FragmentCreatePlaylistBinding::bind)
    private val viewModel: CreatePlaylistViewModel by viewModel()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var strCoverUri = ""
    private val warningDialog: MaterialAlertDialogBuilder by lazy {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle(R.string.warning_finish_creating_playlist)
            .setMessage(R.string.warning_unsaved_data_lost)
            .setNeutralButton(R.string.action_cancel) { _, _ -> }
            .setPositiveButton(R.string.action_finish) { _, _ ->
                findNavController().popBackStack()
            }
    }
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialogOrGoBack()
            }
        }
    private val inputPlaylistNameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(oldText: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(inputText: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(resultText: Editable?) {
            setCreateButtonState(resultText.toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getString(URI)?.let { restoredUri ->
            if (restoredUri.isNotEmpty()) {
                strCoverUri = restoredUri
                showCover(strCoverUri)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                strCoverUri = uri.toString()
                showCover(strCoverUri)
            }
        }
        initListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(URI, strCoverUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
        pickMedia.unregister()
        inputPlaylistNameTextWatcher.let { binding.etPlaylistName.removeTextChangedListener(it) }
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        binding.toolbar.setNavigationOnClickListener { showDialogOrGoBack() }
        binding.ivPlaylistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnCreate.setOnClickListener {
            viewModel.createPlaylist(
                name = binding.etPlaylistName.text.toString(),
                description = binding.etPlaylistDescription.text.toString(),
                strCoverUri = strCoverUri
            )
            findNavController().popBackStack()
            showPlaylistCreatedMessage(binding.etPlaylistName.text.toString())
        }
        binding.etPlaylistName.addTextChangedListener(inputPlaylistNameTextWatcher)
    }

    private fun showPlaylistCreatedMessage(playlistName: String) {
        Toast.makeText(
            requireContext(), getString(R.string.message_success_creation_playlist, playlistName),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setCreateButtonState(playlistName: String) {
        val isNameDuplicated = viewModel.isNameDuplicated(playlistName)
        binding.btnCreate.isEnabled = when {
            !isNameDuplicated && playlistName.isNotEmpty() -> {
                binding.tlPlaylistName.isErrorEnabled = false
                true
            }

            isNameDuplicated -> {
                binding.tlPlaylistName.error = getString(R.string.error_duplicate_playlist)
                false
            }

            else -> {
                binding.tlPlaylistName.isErrorEnabled = false
                false
            }
        }
    }

    private fun showDialogOrGoBack() {
        if ((strCoverUri.toString().isNotEmpty()) ||
            (binding.etPlaylistName.text.toString().trim().isNotEmpty()) ||
            (binding.etPlaylistDescription.text.toString().trim().isNotEmpty())
        ) {
            warningDialog.show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun showCover(uri: String) {
        val cornerRadius = binding.root.resources.getDimensionPixelSize(R.dimen.radius_medium)
        Glide.with(binding.root)
            .load(uri.toUri())
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .placeholder(R.drawable.cover_placeholder)
            .into(binding.ivPlaylistCover)
    }

    companion object {
        private const val URI = "uri"
    }

}