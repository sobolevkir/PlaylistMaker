package com.sobolevkir.playlistmaker

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sobolevkir.playlistmaker.databinding.ActivitySearchBinding
import com.sobolevkir.playlistmaker.ext.closeKeyboard
import com.sobolevkir.playlistmaker.iTunesApi.ITunesApi
import com.sobolevkir.playlistmaker.iTunesApi.TracksResponse
import com.sobolevkir.playlistmaker.tracklist.Track
import com.sobolevkir.playlistmaker.tracklist.TrackListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val OK_RESPONSE_CODE = 200
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTrack() }

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesApi.ITUNES_API_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesApiService = retrofit.create(ITunesApi::class.java)

    private val tracksFound = mutableListOf<Track>()

    private lateinit var binding: ActivitySearchBinding
    private lateinit var foundTracksAdapter: TrackListAdapter
    private lateinit var historyTracksAdapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        with(binding.btnClearQuery) {
            visibility = setClearQueryButtonVisibility(binding.etSearchQuery.text)
            setOnClickListener {
                binding.etSearchQuery.setText("")
                closeKeyboard()
                tracksFound.clear()
                foundTracksAdapter.notifyDataSetChanged()
                binding.rvTrackSearchList.visibility = View.GONE
                binding.tvErrorMessage.visibility = View.GONE
                binding.btnUpdate.visibility = View.GONE
                if (SearchHistory.historyTracks.isNotEmpty()) {
                    historyTracksAdapter.notifyDataSetChanged()
                    binding.vgSearchTrackHistory.visibility = View.VISIBLE
                }
            }
        }

        binding.btnClearHistory.setOnClickListener {
            SearchHistory.clearHistory()
            binding.vgSearchTrackHistory.visibility = View.GONE
        }

        foundTracksAdapter = TrackListAdapter(tracksFound) {
            SearchHistory.addTrackToHistory(it)
            historyTracksAdapter.notifyDataSetChanged()
        }
        historyTracksAdapter = TrackListAdapter(SearchHistory.historyTracks)

        binding.rvTrackSearchList.adapter = foundTracksAdapter
        binding.rvTrackHistoryList.adapter = historyTracksAdapter

        binding.etSearchQuery.setOnFocusChangeListener { _, hasFocus ->
            binding.vgSearchTrackHistory.visibility =
                if (hasFocus && binding.etSearchQuery.text.isEmpty()
                    && SearchHistory.historyTracks.isNotEmpty()
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
                    && SearchHistory.historyTracks.isNotEmpty()
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
        SearchHistory.saveHistory()
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
            iTunesApiService.searchTrack(binding.etSearchQuery.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        binding.progressBar.visibility = View.GONE
                        if (response.code() == OK_RESPONSE_CODE) {
                            tracksFound.clear()
                            binding.btnUpdate.visibility = View.GONE
                            if (response.body()?.results?.isNotEmpty() == true) {
                                binding.tvErrorMessage.visibility = View.GONE
                                tracksFound.addAll(response.body()?.results ?: mutableListOf())
                                foundTracksAdapter.notifyDataSetChanged()
                                binding.rvTrackSearchList.visibility = View.VISIBLE
                            } else {
                                showMessage(
                                    getString(R.string.error_nothing_found),
                                    ContextCompat.getDrawable(
                                        this@SearchActivity,
                                        R.drawable.nothing_found
                                    ), false
                                )
                            }
                        } else {
                            showMessage(
                                getString(R.string.error_connection_problem),
                                ContextCompat.getDrawable(
                                    this@SearchActivity,
                                    R.drawable.connection_problem
                                ), true
                            )
                            binding.btnUpdate.setOnClickListener { searchTrack() }
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        showMessage(
                            getString(R.string.error_connection_problem),
                            ContextCompat.getDrawable(
                                this@SearchActivity,
                                R.drawable.connection_problem
                            ), true
                        )
                    }
                })
        }
    }

    private fun showMessage(errorText: String, errorImage: Drawable?, showUpdateButton: Boolean) {
        tracksFound.clear()
        binding.rvTrackSearchList.visibility = View.GONE
        with(binding.tvErrorMessage) {
            visibility = View.VISIBLE
            text = errorText
            setCompoundDrawablesWithIntrinsicBounds(null, errorImage, null, null)
        }
        binding.btnUpdate.setOnClickListener { searchTrack() }
        if (showUpdateButton) binding.btnUpdate.visibility = View.VISIBLE else View.GONE
    }

    private fun setClearQueryButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}