package com.sobolevkir.playlistmaker.search.domain

import com.sobolevkir.playlistmaker.search.domain.model.ErrorType
import com.sobolevkir.playlistmaker.common.domain.model.Track

interface TracksInteractor {
    fun searchTrack(searchQueryText: String, consumer: TracksConsumer)
    fun getSavedHistory(): MutableList<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistory()

    fun interface TracksConsumer {
        fun consume(tracksFound: List<Track>?, errorType: ErrorType?)
    }
}