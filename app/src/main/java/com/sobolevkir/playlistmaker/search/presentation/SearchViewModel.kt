package com.sobolevkir.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.common.domain.model.ErrorType
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.util.debounce
import com.sobolevkir.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private var latestSearchText: String? = null
    private var isSearchRequestCleared = false
    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { requestText ->
            search(requestText)
        }

    private val stateLiveData = MutableLiveData<SearchState>()
    fun getStateLiveData(): LiveData<SearchState> = stateLiveData

    fun onSearchRequestFieldCleared() {
        isSearchRequestCleared = true
        showHistoryOrDefault()
    }

    fun onClearRequestButtonClick() {
        viewModelScope.coroutineContext[Job]?.cancelChildren()
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
        repeatSearch(requestText = changedText)
    }

    fun repeatSearch(requestText: String) {
        trackSearchDebounce(requestText)
    }

    fun showHistoryOrDefault() {
        viewModelScope.coroutineContext[Job]?.cancelChildren()
        val historyTracks = tracksInteractor.getSavedHistory()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchState.History(historyTracks))
        } else {
            renderState(SearchState.Default)
        }
    }

    private fun search(newRequestText: String) {
        if (newRequestText.isNotEmpty()) {
            renderState(SearchState.Loading)
            tracksInteractor.searchTrack(newRequestText) { tracksFound, errorType ->
                if (!isSearchRequestCleared) {
                    when (errorType) {
                        ErrorType.SERVER_ERROR,
                        ErrorType.BAD_REQUEST,
                        ErrorType.CONNECTION_PROBLEM -> renderState(SearchState.Error)

                        ErrorType.NOTHING_FOUND -> renderState(SearchState.NothingFound)
                        null -> {
                            val result =
                                tracksFound?.sortedByDescending { track -> track.isFavorite }
                                    ?: listOf()
                            renderState(SearchState.SearchResult(result))
                        }
                    }
                }
            }
        }
        isSearchRequestCleared = false
    }

    private fun renderState(state: SearchState) = stateLiveData.postValue(state)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

}