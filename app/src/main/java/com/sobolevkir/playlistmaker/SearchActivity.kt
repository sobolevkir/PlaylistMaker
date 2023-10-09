package com.sobolevkir.playlistmaker

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sobolevkir.playlistmaker.iTunesAPI.ITunesApi
import com.sobolevkir.playlistmaker.iTunesAPI.TracksResponse
import com.sobolevkir.playlistmaker.tracklist.Track
import com.sobolevkir.playlistmaker.tracklist.TrackListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val ITUNES_API_BASEURL = "https://itunes.apple.com"
        private const val OK_RESPONSE_CODE = 200
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_API_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesApiService = retrofit.create(ITunesApi::class.java)

    private val tracksFound = mutableListOf<Track>()
    private val searchHistory = SearchHistory()

    private lateinit var adapterFoundTracks: TrackListAdapter
    private lateinit var searchQueryInput: EditText
    private lateinit var trackSearchList: RecyclerView
    private lateinit var historyList: RecyclerView
    private lateinit var historyContainer: ViewGroup
    private lateinit var backButton: ImageButton
    private lateinit var clearButton: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var updateButton: Button
    private lateinit var clearHistoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchQueryInput = findViewById(R.id.et_search_query)
        backButton = findViewById(R.id.btn_back)
        clearButton = findViewById(R.id.btn_clear)
        trackSearchList = findViewById(R.id.rv_track_search_list)
        historyList = findViewById(R.id.rv_track_history_list)
        historyContainer = findViewById(R.id.vg_search_track_history)
        errorMessage = findViewById(R.id.tv_error_message)
        updateButton = findViewById(R.id.btn_update)
        clearHistoryButton = findViewById(R.id.btn_clear_history)

        backButton.setOnClickListener { finish() }
        clearButton.visibility = setClearButtonVisibility(searchQueryInput.text)
        clearButton.setOnClickListener {
            searchQueryInput.setText("")
            Utils.closeKeyboard(this@SearchActivity, applicationContext)
            tracksFound.clear()
            adapterFoundTracks.notifyDataSetChanged()
            trackSearchList.visibility = View.GONE
            errorMessage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            historyContainer.visibility = View.GONE
        }

        adapterFoundTracks = TrackListAdapter(tracksFound) {
            searchHistory.addTrackToHistory(it)
            Toast.makeText(
                applicationContext, "Трек ${it.trackName} добавлен в историю",
                Toast.LENGTH_SHORT
            ).show()
        }
        trackSearchList.adapter = adapterFoundTracks
        searchHistory.readSavedHistory()
        historyList.adapter = searchHistory.adapterHistoryTracks

        searchQueryInput.setOnFocusChangeListener { _, hasFocus ->
            historyContainer.visibility =
                if (hasFocus && searchQueryInput.text.isEmpty()
                    && searchHistory.historyTracks.isNotEmpty()) View.VISIBLE else View.GONE
        }

        searchQueryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
            }
            false
        }

        val searchInputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = setClearButtonVisibility(s)
                historyContainer.visibility =
                    if (searchQueryInput.hasFocus() && s?.isEmpty() == true
                        && searchHistory.historyTracks.isNotEmpty()) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        searchQueryInput.addTextChangedListener(searchInputTextWatcher)

    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveHistory()
    }

    private fun searchTrack() {
        if (searchQueryInput.text.isNotEmpty()) {
            iTunesApiService.searchTrack(searchQueryInput.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.code() == OK_RESPONSE_CODE) {
                            tracksFound.clear()
                            updateButton.visibility = View.GONE
                            if (response.body()?.results?.isNotEmpty() == true) {
                                errorMessage.visibility = View.GONE
                                tracksFound.addAll(response.body()?.results ?: mutableListOf())
                                adapterFoundTracks.notifyDataSetChanged()
                                trackSearchList.visibility = View.VISIBLE
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
                            updateButton.setOnClickListener { searchTrack() }
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        trackSearchList.visibility = View.GONE
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
        trackSearchList.visibility = View.GONE
        errorMessage.visibility = View.VISIBLE
        errorMessage.text = errorText
        errorMessage.setCompoundDrawablesWithIntrinsicBounds(null, errorImage, null, null)
        updateButton.setOnClickListener { searchTrack() }
        if(showUpdateButton) updateButton.visibility = View.VISIBLE else View.GONE
    }

    private fun setClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}