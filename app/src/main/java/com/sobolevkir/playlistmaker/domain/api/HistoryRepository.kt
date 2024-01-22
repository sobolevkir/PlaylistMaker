package com.sobolevkir.playlistmaker.domain.api

import com.sobolevkir.playlistmaker.domain.models.Track

interface HistoryRepository {

    fun saveHistoryToLocalStorage()

    fun getSavedHistory(): MutableList<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistory()

}