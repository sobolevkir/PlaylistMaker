package com.sobolevkir.playlistmaker

import com.google.gson.Gson
import com.sobolevkir.playlistmaker.PrefsManager.TRACK_HISTORY_LIST
import com.sobolevkir.playlistmaker.tracklist.Track

object SearchHistory {

    val historyTracks = mutableListOf<Track>()

    fun readSavedHistory() {
        val history = PrefsManager.read(TRACK_HISTORY_LIST, null)
        if (history != null) historyTracks.addAll(Gson().fromJson(history, Array<Track>::class.java))
    }

    fun saveHistory() {
        PrefsManager.write(TRACK_HISTORY_LIST, Gson().toJson(historyTracks))
    }

    fun addTrackToHistory(track: Track) {
        with(historyTracks) {
            if (this.contains(track)) this.remove(track)
            this.add(0, track)
            if (this.size > 10) this.removeLast()
        }
    }

    fun clearHistory() {
        historyTracks.clear()
    }

}