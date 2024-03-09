package com.sobolevkir.playlistmaker.search.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.search.domain.TracksInteractor
import com.sobolevkir.playlistmaker.search.domain.model.ErrorType
import com.sobolevkir.playlistmaker.search.ui.model.SearchState

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var latestSearchText: String? = null
    private var isSearchRequestCleared = false
    private var isClickAllowed = true

    private val stateLiveData = MutableLiveData<SearchState>()
    fun getStateLiveData(): LiveData<SearchState> = stateLiveData

    fun onSearchRequestFieldCleared() {
        isSearchRequestCleared = true
        showHistoryOrDefault()
    }

    fun onClearRequestButtonClick() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        isSearchRequestCleared = true
        renderState(SearchState.Default)
    }

    fun onClearHistoryButtonClick() {
        tracksInteractor.clearHistory()
        renderState(SearchState.Default)
    }

    fun onFoundTrackClick(track: Track) {
        tracksInteractor.addTrackToHistory(track)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return
        this.latestSearchText = changedText
        repeatSearch(changedText)
    }

    fun repeatSearch(requestText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { search(requestText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    fun showHistoryOrDefault() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val historyTracks = tracksInteractor.getSavedHistory()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchState.History(historyTracks))
        } else {
            renderState(SearchState.Default)
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true }, CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    private fun search(newRequestText: String) {
        if (newRequestText.isNotEmpty()) {
            renderState(SearchState.Loading)
            tracksInteractor.searchTrack(newRequestText, object : TracksInteractor.TracksConsumer {
                override fun consume(tracksFound: List<Track>?, errorType: ErrorType?) {
                    if (!isSearchRequestCleared) {
                        val tracks = mutableListOf<Track>()
                        tracksFound?.let { tracks.addAll(it) }
                        when (errorType) {
                            ErrorType.SERVER_ERROR, ErrorType.BAD_REQUEST, ErrorType.CONNECTION_PROBLEM -> renderState(
                                SearchState.Error
                            )

                            ErrorType.NOTHING_FOUND -> renderState(SearchState.NothingFound)
                            null -> renderState(SearchState.SearchResult(tracks.sortedByDescending { it.isFavorite }))
                        }
                    }
                }
            })
        }
        isSearchRequestCleared = false
    }

    private fun renderState(state: SearchState) = stateLiveData.postValue(state)

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 500L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

}