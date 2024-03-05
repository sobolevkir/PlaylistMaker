package com.sobolevkir.playlistmaker.search.data.impl

import com.google.gson.Gson
import com.sobolevkir.playlistmaker.common.domain.LocalStorage
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.search.data.mapper.TrackMapper
import com.sobolevkir.playlistmaker.search.data.model.ResultCode
import com.sobolevkir.playlistmaker.search.data.network.NetworkClient
import com.sobolevkir.playlistmaker.search.data.network.TracksSearchRequest
import com.sobolevkir.playlistmaker.search.data.network.TracksSearchResponse
import com.sobolevkir.playlistmaker.search.domain.TracksRepository
import com.sobolevkir.playlistmaker.search.domain.model.ErrorType
import com.sobolevkir.playlistmaker.search.domain.model.Resource

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val gson: Gson
) : TracksRepository {
    override fun searchTrack(searchQueryText: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(searchQueryText))
        return when (response.resultCode) {
            ResultCode.CONNECTION_PROBLEM_CODE -> Resource.Error(ErrorType.CONNECTION_PROBLEM)
            ResultCode.SUCCESS_CODE -> {
                if ((response as TracksSearchResponse).results.isEmpty()) {
                    Resource.Error(ErrorType.NOTHING_FOUND)
                } else {
                    Resource.Success(TrackMapper.map(response.results))
                }
            }

            ResultCode.BAD_REQUEST_CODE -> Resource.Error(ErrorType.BAD_REQUEST)
            ResultCode.NOTHING_FOUND_CODE -> Resource.Error(ErrorType.NOTHING_FOUND)

            else -> Resource.Error(ErrorType.SERVER_ERROR)
        }
    }

    override fun getSavedHistory(): MutableList<Track> {
        val history = localStorage.read(TRACK_HISTORY_LIST, null)
        if (history != null) {
            return gson.fromJson(history, Array<Track>::class.java).toMutableList()
        }
        return mutableListOf()
    }

    override fun addTrackToHistory(track: Track) {
        val historyTracks = getSavedHistory()
        with(historyTracks) {
            this.removeIf { it.trackId == track.trackId }
            this.add(0, track)
            if (this.size > HISTORY_LIST_MAX_SIZE) {
                this.removeLast()
            }
        }
        localStorage.write(TRACK_HISTORY_LIST, gson.toJson(historyTracks))
    }

    override fun clearHistory() {
        localStorage.write(TRACK_HISTORY_LIST, gson.toJson(emptyList<Track>()))
    }

    companion object {
        private const val TRACK_HISTORY_LIST = "track_history_list"
        private const val HISTORY_LIST_MAX_SIZE = 10
    }
}
