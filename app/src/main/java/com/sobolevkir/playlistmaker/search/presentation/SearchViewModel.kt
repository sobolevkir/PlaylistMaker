package com.sobolevkir.playlistmaker.search.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel(),
    DefaultLifecycleObserver {

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { requestText ->
            search(requestText.trim())
        }

    private val stateLiveData = MutableLiveData<SearchState>()
    fun getStateLiveData(): LiveData<SearchState> = stateLiveData

    fun onClearRequestButtonClick() {
        viewModelScope.coroutineContext[Job]?.cancelChildren()
        renderState(SearchState.Default)
    }

    fun onClearHistoryButtonClick() {
        tracksInteractor.clearHistory()
        renderState(SearchState.Default)
    }

    fun onFoundTrackClick(track: Track) {
        viewModelScope.launch { tracksInteractor.addTrackToHistory(track) }
    }

    fun showHistoryOrDefault() {
        viewModelScope.coroutineContext[Job]?.cancelChildren()
        viewModelScope.launch {
            val historyTracks = tracksInteractor.getSavedHistory()
            if (historyTracks.isNotEmpty()) {
                renderState(SearchState.History(historyTracks))
            } else {
                renderState(SearchState.Default)
            }
        }
    }

    fun searchDebounce(requestText: String) = trackSearchDebounce(requestText)

    private fun search(newRequestText: String) {
        if (newRequestText.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                tracksInteractor
                    .searchTrack(newRequestText)
                    .collect { (tracksFound, errorType) ->
                        processResult(tracksFound, errorType)
                    }
            }
        }
    }

    private fun processResult(tracksFound: List<Track>?, errorType: ErrorType?) {
        when (errorType) {
            ErrorType.SERVER_ERROR,
            ErrorType.BAD_REQUEST,
            ErrorType.CONNECTION_PROBLEM -> renderState(SearchState.Error)

            ErrorType.NOTHING_FOUND -> renderState(SearchState.NothingFound)
            null -> {
                val result =
                    tracksFound?.sortedByDescending {
                        it.isFavorite
                    } ?: listOf()
                renderState(SearchState.SearchResult(result))
            }
        }
    }

    private fun renderState(state: SearchState) = stateLiveData.postValue(state)

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (stateLiveData.value is SearchState.History) {
            showHistoryOrDefault()
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

}