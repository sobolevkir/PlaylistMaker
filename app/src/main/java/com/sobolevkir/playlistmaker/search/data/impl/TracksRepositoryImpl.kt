package com.sobolevkir.playlistmaker.search.data.impl

import com.google.gson.Gson
import com.sobolevkir.playlistmaker.common.domain.LocalStorage
import com.sobolevkir.playlistmaker.common.domain.model.ErrorType
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.util.Resource
import com.sobolevkir.playlistmaker.search.data.dto.TracksSearchRequest
import com.sobolevkir.playlistmaker.search.data.dto.TracksSearchResponse
import com.sobolevkir.playlistmaker.search.data.mapper.TrackMapper
import com.sobolevkir.playlistmaker.search.data.network.NetworkClient
import com.sobolevkir.playlistmaker.search.data.network.ResultCode
import com.sobolevkir.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val gson: Gson
) : TracksRepository {
    override fun searchTrack(searchQueryText: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(searchQueryText))
        when (response.resultCode) {
            ResultCode.CONNECTION_PROBLEM_CODE -> emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.SUCCESS_CODE -> {
                if ((response as TracksSearchResponse).results.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    emit(Resource.Success(TrackMapper.map(response.results)))
                }
            }

            ResultCode.BAD_REQUEST_CODE -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND_CODE -> emit(Resource.Error(ErrorType.NOTHING_FOUND))

            else -> emit(Resource.Error(ErrorType.SERVER_ERROR))
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
