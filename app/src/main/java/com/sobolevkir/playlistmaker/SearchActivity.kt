package com.sobolevkir.playlistmaker

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

    private val iTunesAPIBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesAPIBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesApiService = retrofit.create(ITunesApi::class.java)
    private val adapter = TrackListAdapter()
    private val tracks = mutableListOf<Track>()

    private var savedSearchQueryText = ""

    private lateinit var searchQueryInput: EditText
    private lateinit var trackSearchList: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var clearButton: ImageView
    private lateinit var errorMessage: TextView
    private lateinit var updateButton: Button

    companion object {
        private const val SEARCH_QUERY_TEXT = "SEARCH_QUERY_TEXT"
        private const val OK_RESPONSE = 200
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(
            SEARCH_QUERY_TEXT,
            savedSearchQueryText
        )
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedSearchQueryText = savedInstanceState.getString(SEARCH_QUERY_TEXT, "")
        searchQueryInput.setText(savedSearchQueryText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchQueryInput = findViewById(R.id.et_search_query)
        backButton = findViewById(R.id.btn_back)
        clearButton = findViewById(R.id.btn_clear)
        trackSearchList = findViewById(R.id.rv_search_track_list)
        errorMessage = findViewById(R.id.tv_error_message)
        updateButton = findViewById(R.id.btn_update)

        adapter.tracks = tracks
        trackSearchList.adapter = adapter

        backButton.setOnClickListener { finish() }
        clearButton.visibility = clearButtonVisibility(searchQueryInput.text)
        clearButton.setOnClickListener {
            searchQueryInput.setText("")
            closeKeyboard()
            tracks.clear()
            adapter.notifyDataSetChanged()
            errorMessage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }

        searchQueryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        val searchInputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                savedSearchQueryText = searchQueryInput.text.toString()
            }
        }
        searchQueryInput.addTextChangedListener(searchInputTextWatcher)

    }

    private fun search() {
        if (searchQueryInput.text.isNotEmpty()) {
            iTunesApiService.searchTrack(searchQueryInput.text.toString())
                .enqueue(object : Callback<TracksResponse> {
                    override fun onResponse(
                        call: Call<TracksResponse>,
                        response: Response<TracksResponse>
                    ) {
                        if (response.code() == OK_RESPONSE) {
                            tracks.clear()
                            updateButton.visibility = View.GONE
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results ?: mutableListOf<Track>())
                                adapter.notifyDataSetChanged()
                            }
                            else {
                                showMessage(getString(R.string.error_nothing_found),
                                    ContextCompat.getDrawable(applicationContext, R.drawable.nothing_found))
                            }
                        } else {
                            showMessage(getString(R.string.error_nothing_found),
                                ContextCompat.getDrawable(applicationContext, R.drawable.nothing_found))
                        }
                    }

                    override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                        showMessage(getString(R.string.error_connection_problem),
                            ContextCompat.getDrawable(applicationContext, R.drawable.connection_problem))
                        updateButton.setOnClickListener{search()}
                        updateButton.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun showMessage(errorText: String, errorImage: Drawable?) {
        if (errorText.isNotEmpty()) {
            errorMessage.visibility = View.VISIBLE
            tracks.clear()
            adapter.notifyDataSetChanged()
            errorMessage.text = errorText
            errorMessage.setCompoundDrawablesWithIntrinsicBounds(null, errorImage,null,null)
        } else {
            errorMessage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}