package com.sobolevkir.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.ext.hideKeyboard
import com.sobolevkir.playlistmaker.common.ext.showKeyboard
import com.sobolevkir.playlistmaker.common.ui.TrackListAdapter
import com.sobolevkir.playlistmaker.databinding.FragmentSearchBinding
import com.sobolevkir.playlistmaker.search.presentation.SearchState
import com.sobolevkir.playlistmaker.search.presentation.SearchViewModel
import com.sobolevkir.playlistmaker.common.util.debounce
import com.sobolevkir.playlistmaker.common.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel by viewModel<SearchViewModel>()
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private var foundTracksAdapter: TrackListAdapter? = null
    private var historyTracksAdapter: TrackListAdapter? = null
    private var searchTextWatcher: TextWatcher? = null
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun onStart() {
        super.onStart()
        binding.etSearchRequest.requestFocus()
        activity?.showKeyboard()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initClickDebounce()
        initListeners()
        initTextWatchers()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        foundTracksAdapter = null
        historyTracksAdapter = null
        binding.rvTrackSearchList.adapter = null
        binding.rvTrackHistoryList.adapter = null
        searchTextWatcher?.let { binding.etSearchRequest.removeTextChangedListener(it) }
    }

    private fun initClickDebounce() {
        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track -> openPlayer(track) }
    }

    private fun initAdapters() {
        foundTracksAdapter = TrackListAdapter(onItemClick = {
            viewModel.onFoundTrackClick(it)
            onTrackClickDebounce(it)
        })
        historyTracksAdapter = TrackListAdapter(onItemClick = { onTrackClickDebounce(it) })
        with(binding) {
            rvTrackSearchList.adapter = foundTracksAdapter
            rvTrackHistoryList.adapter = historyTracksAdapter
        }
    }

    private fun initListeners() {
        with(binding.btnClearRequest) {
            setOnClickListener {
                activity?.hideKeyboard()
                viewModel.onClearRequestButtonClick()
                binding.etSearchRequest.apply {
                    clearFocus()
                    setText("")
                }
            }
        }
        binding.btnClearHistory.setOnClickListener { viewModel.onClearHistoryButtonClick() }
        binding.btnUpdate.setOnClickListener {
            viewModel.searchDebounce(binding.etSearchRequest.text.toString())
        }
        binding.etSearchRequest.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) view.clearFocus()
            false
        }
        binding.etSearchRequest.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etSearchRequest.text.isEmpty()) {
                viewModel.showHistoryOrDefault()
            }
        }
        binding.vgSearchTrackHistory.setOnScrollChangeListener { _: View, _: Int, _: Int, _: Int, _: Int ->
            activity?.hideKeyboard()
            binding.etSearchRequest.clearFocus()
        }
    }

    private fun initTextWatchers() {
        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.btnClearRequest.isVisible = false
                    if (binding.etSearchRequest.hasFocus()) {
                        viewModel.showHistoryOrDefault()
                    }
                } else {
                    binding.btnClearRequest.isVisible = true
                    viewModel.searchDebounce(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        searchTextWatcher?.let { binding.etSearchRequest.addTextChangedListener(it) }
    }

    private fun initObservers() {
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { state -> render(state) }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Default -> showDefault()
            is SearchState.History -> showHistory(state.historyTracks)
            is SearchState.Error -> showError()
            is SearchState.NothingFound -> showNothingFound()
            is SearchState.Loading -> showLoading()
            is SearchState.SearchResult -> showSearchResult(state.tracks)
        }
    }

    private fun showDefault() {
        with(binding) {
            btnUpdate.isVisible = false
            vgSearchTrackHistory.isVisible = false
            tvErrorMessage.isVisible = false
            progressBar.isVisible = false
            rvTrackSearchList.isVisible = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchResult(tracks: List<Track>) {
        with(foundTracksAdapter) {
            this?.tracks?.clear()
            this?.tracks?.addAll(tracks)
            this?.notifyDataSetChanged()
        }
        with(binding) {
            btnUpdate.isVisible = false
            vgSearchTrackHistory.isVisible = false
            tvErrorMessage.isVisible = false
            progressBar.isVisible = false
            rvTrackSearchList.isVisible = true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory(historyTracks: List<Track>) {
        with(historyTracksAdapter) {
            this?.tracks?.clear()
            this?.tracks?.addAll(historyTracks)
            this?.notifyDataSetChanged()
        }
        with(binding) {
            btnUpdate.isVisible = false
            tvErrorMessage.isVisible = false
            progressBar.isVisible = false
            rvTrackSearchList.isVisible = false
            vgSearchTrackHistory.isVisible = true
        }
    }

    private fun showError() {
        with(binding) {
            btnUpdate.isVisible = true
            vgSearchTrackHistory.isVisible = false
            rvTrackSearchList.isVisible = false
            progressBar.isVisible = false
        }
        with(binding.tvErrorMessage) {
            isVisible = true
            text = getString(R.string.error_connection_problem)
            val errorImage =
                ContextCompat.getDrawable(requireContext(), R.drawable.connection_problem)
            setCompoundDrawablesWithIntrinsicBounds(null, errorImage, null, null)
        }
    }

    private fun showNothingFound() {
        with(binding) {
            btnUpdate.isVisible = false
            vgSearchTrackHistory.isVisible = false
            rvTrackSearchList.isVisible = false
            progressBar.isVisible = false
        }
        with(binding.tvErrorMessage) {
            isVisible = true
            text = getString(R.string.error_nothing_found)
            val errorImage =
                ContextCompat.getDrawable(requireContext(), R.drawable.nothing_found)
            setCompoundDrawablesWithIntrinsicBounds(null, errorImage, null, null)
        }
    }

    private fun showLoading() {
        with(binding) {
            btnUpdate.isVisible = false
            vgSearchTrackHistory.isVisible = false
            rvTrackSearchList.isVisible = false
            tvErrorMessage.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun openPlayer(track: Track) {
        val action = SearchFragmentDirections.actionSearchFragmentToPlayerActivity(track)
        findNavController().navigate(action)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
    }

}