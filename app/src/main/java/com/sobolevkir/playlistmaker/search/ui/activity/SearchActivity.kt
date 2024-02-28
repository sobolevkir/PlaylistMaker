package com.sobolevkir.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.databinding.ActivitySearchBinding
import com.sobolevkir.playlistmaker.ext.hideKeyboard
import com.sobolevkir.playlistmaker.search.ui.adapter.TrackListAdapter
import com.sobolevkir.playlistmaker.search.ui.model.SearchState
import com.sobolevkir.playlistmaker.search.ui.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> { SearchViewModel.getViewModelFactory() }
    private val foundTracksAdapter = TrackListAdapter {
        viewModel.onFoundTrackClick(it)
        viewModel.openPlayer(it)
    }
    private val historyTracksAdapter = TrackListAdapter {
        viewModel.openPlayer(it)
    }
    private var searchTextWatcher: TextWatcher? = null
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapters()
        initListeners()
        if (savedInstanceState == null) binding.etSearchRequest.requestFocus()
        initTextWatchers()
        viewModel.getStateLiveData().observe(this) { render(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchTextWatcher?.let { binding.etSearchRequest.removeTextChangedListener(it) }
    }

    private fun initListeners() {
        with(binding.btnClearRequest) {
            setOnClickListener {
                hideKeyboard()
                viewModel.onClearRequestButtonClick()
                binding.etSearchRequest.apply {
                    clearFocus()
                    setText("")
                }
            }
        }
        binding.btnBack.setOnClickListener { finish() }
        binding.btnClearHistory.setOnClickListener { viewModel.onClearHistoryButtonClick() }
        binding.btnUpdate.setOnClickListener {
            viewModel.repeatSearch(binding.etSearchRequest.text.toString().trim())
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
    }

    private fun initTextWatchers() {
        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.btnClearRequest.isVisible = false
                    if (binding.etSearchRequest.hasFocus()) {
                        viewModel.onSearchRequestFieldCleared()
                    }
                } else {
                    binding.btnClearRequest.isVisible = true
                    viewModel.searchDebounce(s.toString().trim())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        searchTextWatcher?.let { binding.etSearchRequest.addTextChangedListener(it) }
    }

    private fun initAdapters() {
        with(binding) {
            rvTrackSearchList.adapter = foundTracksAdapter
            rvTrackHistoryList.adapter = historyTracksAdapter
        }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.Default -> showDefault()
            is SearchState.SearchResult -> showSearchResult(state.tracks)
            is SearchState.History -> showHistory(state.historyTracks)
            is SearchState.Error -> showError()
            is SearchState.NothingFound -> showNothingFound()
            is SearchState.Loading -> showLoading()
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
            this.tracks.clear()
            this.tracks.addAll(tracks)
            notifyDataSetChanged()
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
            this.tracks.clear()
            this.tracks.addAll(historyTracks)
            notifyDataSetChanged()
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
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.connection_problem)
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
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.nothing_found)
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

    companion object {
        const val CURRENT_TRACK = "current_track"
    }


}