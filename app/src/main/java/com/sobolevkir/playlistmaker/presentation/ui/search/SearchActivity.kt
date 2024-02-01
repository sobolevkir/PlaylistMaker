package com.sobolevkir.playlistmaker.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.sobolevkir.playlistmaker.databinding.ActivitySearchBinding
import com.sobolevkir.playlistmaker.domain.api.TracksInteractor
import com.sobolevkir.playlistmaker.domain.models.Track
import com.sobolevkir.playlistmaker.presentation.ui.ext.closeKeyboard
import com.sobolevkir.playlistmaker.presentation.ui.player.PlayerActivity
import com.sobolevkir.playlistmaker.presentation.ui.track_list.TrackListAdapter
import com.sobolevkir.playlistmaker.util.Creator

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        const val CURRENT_TRACK = "current_track"
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTrack() }

    private val foundTracksList = mutableListOf<Track>()

    private var tracksInteractor = Creator.provideTracksInteractor()
    private val localStorage = Creator.provideLocalStorage()
    private val historyInteractor = Creator.provideHistoryInteractor(localStorage)

    private lateinit var binding: ActivitySearchBinding
    private lateinit var foundTracksAdapter: TrackListAdapter
    private lateinit var historyTracksAdapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val historyTracksList = historyInteractor.getSavedHistory()

        with(binding.btnClearQuery) {
            visibility = setClearQueryButtonVisibility(binding.etSearchQuery.text)
            setOnClickListener {
                binding.etSearchQuery.setText("")
                closeKeyboard()
                foundTracksList.clear()
                foundTracksAdapter.notifyDataSetChanged()
                binding.rvTrackSearchList.visibility = View.GONE
                binding.tvErrorMessage.visibility = View.GONE
                binding.btnUpdate.visibility = View.GONE
                if (historyTracksList.isNotEmpty()) {
                    historyTracksAdapter.notifyDataSetChanged()
                    binding.vgSearchTrackHistory.visibility = View.VISIBLE
                }
            }
        }

        binding.btnClearHistory.setOnClickListener {
            historyInteractor.clearHistory()
            binding.vgSearchTrackHistory.visibility = View.GONE
        }

        foundTracksAdapter = TrackListAdapter(foundTracksList) {
            historyInteractor.addTrackToHistory(it)
            openPlayer(it)
        }
        historyTracksAdapter = TrackListAdapter(historyTracksList) {
            openPlayer(it)
        }

        binding.rvTrackSearchList.adapter = foundTracksAdapter
        binding.rvTrackHistoryList.adapter = historyTracksAdapter

        binding.etSearchQuery.setOnFocusChangeListener { _, hasFocus ->
            binding.vgSearchTrackHistory.visibility =
                if (hasFocus && binding.etSearchQuery.text.isEmpty()
                    && historyTracksList.isNotEmpty()
                ) View.VISIBLE else View.GONE
        }
        binding.etSearchQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etSearchQuery.clearFocus()
                handler.removeCallbacks(searchRunnable)
                searchTrack()
            }
            false
        }

        val searchInputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnClearQuery.visibility = setClearQueryButtonVisibility(s)
                searchDebounce()
                if (binding.etSearchQuery.hasFocus() && s?.isEmpty() == true
                    && historyTracksList.isNotEmpty()
                ) {
                    binding.tvErrorMessage.visibility = View.GONE
                    binding.btnUpdate.visibility = View.GONE
                    binding.rvTrackSearchList.visibility = View.GONE
                    binding.vgSearchTrackHistory.visibility = View.VISIBLE
                } else {
                    binding.vgSearchTrackHistory.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.etSearchQuery.addTextChangedListener(searchInputTextWatcher)

    }

    override fun onStart() {
        super.onStart()
        binding.etSearchQuery.requestFocus()
    }

    override fun onStop() {
        super.onStop()
        historyInteractor.saveHistoryToLocalStorage()
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchTrack() {
        if (binding.etSearchQuery.text.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvTrackSearchList.visibility = View.GONE
            binding.tvErrorMessage.visibility = View.GONE
            binding.btnUpdate.visibility = View.GONE

            tracksInteractor.searchTrack(
                binding.etSearchQuery.text.toString().trim(),
                object : TracksInteractor.TracksConsumer {
                    override fun consume(tracksFound: List<Track>?) {
                        if (tracksFound != null) {
                            foundTracksList.clear()
                            foundTracksList.addAll(tracksFound)
                        }
                        runOnUiThread {
                            foundTracksAdapter.notifyDataSetChanged()
                            binding.progressBar.visibility = View.GONE
                            binding.rvTrackSearchList.visibility = View.VISIBLE
                        }
                    }
                }
            )
        }
    }

    private fun setClearQueryButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun openPlayer(track: Track) {
        if (clickDebounce()) {
            localStorage.write(CURRENT_TRACK, Gson().toJson(track))
            val playerIntent = Intent(this, PlayerActivity::class.java)
            startActivity(playerIntent)
        }
    }

}