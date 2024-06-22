package com.sobolevkir.playlistmaker.search.presentation

import com.sobolevkir.playlistmaker.common.domain.model.Track

sealed interface SearchState {

    data object Default : SearchState
    data object Loading : SearchState
    data object Error : SearchState
    data object NothingFound : SearchState

    data class SearchResult(
        val tracks: List<Track>
    ) : SearchState

    data class History(
        val historyTracks: List<Track>
    ) : SearchState

}