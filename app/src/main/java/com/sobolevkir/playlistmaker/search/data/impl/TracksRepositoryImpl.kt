package com.sobolevkir.playlistmaker.search.data.impl

import com.google.gson.Gson
import com.sobolevkir.playlistmaker.common.data.db.AppDatabase
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
    private val gson: Gson,
    private val appDatabase: AppDatabase
) : TracksRepository {

    override fun searchTrack(searchQueryText: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(searchQueryText))
        when (response.resultCode) {
            ResultCode.CONNECTION_PROBLEM_CODE -> emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.SUCCESS_CODE -> {
                if ((response as TracksSearchResponse).results.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    val foundTracks = TrackMapper.map(response.results)
                    val tracksWithFavoriteFlag = foundTracks.map { track ->
                        track.copy(isFavorite = isTrackFavorite(track))
                    }
                    emit(Resource.Success(tracksWithFavoriteFlag))
                }
            }

            ResultCode.BAD_REQUEST_CODE -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND_CODE -> emit(Resource.Error(ErrorType.NOTHING_FOUND))

            else -> emit(Resource.Error(ErrorType.SERVER_ERROR))
        }
    }

    override suspend fun getSavedHistory(): List<Track> {
        val history = localStorage.read(TRACK_HISTORY_LIST, "")
        if (history.isNotEmpty()) {
            val historyTracks = gson.fromJson(history, Array<Track>::class.java)
            return historyTracks.map { track -> track.copy(isFavorite = isTrackFavorite(track)) }
        }
        return mutableListOf()
    }

    override suspend fun addTrackToHistory(track: Track) {
        val historyTracks = getSavedHistory().toMutableList()
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
        localStorage.write(TRACK_HISTORY_LIST, "")
    }

    private suspend fun isTrackFavorite(track: Track): Boolean {
        val favoriteTracksIds = appDatabase.getFavoriteTrackDao().getTracksIds()
        return favoriteTracksIds.contains(track.trackId)
    }

    companion object {
        private const val TRACK_HISTORY_LIST = "track_history_list"
        private const val HISTORY_LIST_MAX_SIZE = 10
    }

}
