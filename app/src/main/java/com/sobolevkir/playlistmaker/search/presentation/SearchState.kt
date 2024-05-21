package com.sobolevkir.playlistmaker.search.presentation

import com.sobolevkir.playlistmaker.common.domain.model.Track

sealed interface SearchState {

    object Default : SearchState
    object Loading : SearchState
    object Error : SearchState
    object NothingFound : SearchState

    data class SearchResult(
        val tracks: List<Track>
    ) : SearchState

    data class History(
        val historyTracks: List<Track>
    ) : SearchState

}