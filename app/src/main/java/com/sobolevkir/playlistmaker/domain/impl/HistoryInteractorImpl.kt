package com.sobolevkir.playlistmaker.domain.impl

import com.sobolevkir.playlistmaker.domain.api.HistoryInteractor
import com.sobolevkir.playlistmaker.domain.api.HistoryRepository
import com.sobolevkir.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val repository: HistoryRepository): HistoryInteractor {
    override fun saveHistoryToLocalStorage() {
        repository.saveHistoryToLocalStorage()
    }

    override fun getSavedHistory(): MutableList<Track> {
        return repository.getSavedHistory()
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}