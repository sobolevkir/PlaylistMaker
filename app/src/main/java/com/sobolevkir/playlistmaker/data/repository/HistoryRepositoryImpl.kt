package com.sobolevkir.playlistmaker.data.repository

import com.google.gson.Gson
import com.sobolevkir.playlistmaker.domain.api.HistoryRepository
import com.sobolevkir.playlistmaker.domain.api.LocalStorage
import com.sobolevkir.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(private val localStorage: LocalStorage) : HistoryRepository {

    private val historyTracks = mutableListOf<Track>()

    override fun saveHistoryToLocalStorage() =
        localStorage.write(TRACK_HISTORY_LIST, Gson().toJson(historyTracks))

    override fun getSavedHistory(): MutableList<Track> {
        val history = localStorage.read(TRACK_HISTORY_LIST, null)
        historyTracks.clear()
        if (history != null) historyTracks.addAll(
            Gson().fromJson(history, Array<Track>::class.java)
        )
        return historyTracks
    }

    override fun addTrackToHistory(track: Track) {
        with(historyTracks) {
            if (this.contains(track)) this.remove(track)
            this.add(0, track)
            if (this.size > HISTORY_LIST_MAX_SIZE) this.removeLast()
        }
    }

    override fun clearHistory() {
        historyTracks.clear()
    }

    companion object {
        private const val TRACK_HISTORY_LIST = "track_history_list"
        private const val HISTORY_LIST_MAX_SIZE = 10
    }

}
