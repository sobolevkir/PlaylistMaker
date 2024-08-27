package com.sobolevkir.playlistmaker.search.data.impl

import com.google.gson.Gson
import com.sobolevkir.playlistmaker.common.data.AppDatabase
import com.sobolevkir.playlistmaker.common.domain.LocalStorage
import com.sobolevkir.playlistmaker.search.domain.model.ErrorType
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.util.Resource
import com.sobolevkir.playlistmaker.search.data.dto.TracksSearchRequest
import com.sobolevkir.playlistmaker.search.data.dto.TracksSearchResponse
import com.sobolevkir.playlistmaker.search.data.mapper.TrackMapper
import com.sobolevkir.playlistmaker.search.data.network.NetworkClient
import com.sobolevkir.playlistmaker.search.data.network.ResultCode
import com.sobolevkir.playlistmaker.search.domain.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val gson: Gson,
    private val appDatabase: AppDatabase
) : TracksRepository {

    private val historyList = mutableListOf<Track>()

    init {
        val history = localStorage.read(TRACK_HISTORY_LIST, "")
        if (history.isNotEmpty()) {
            val historyTracks = gson.fromJson(history, Array<Track>::class.java).toList()
            historyList.addAll(historyTracks)
        }
    }

    override fun searchTrack(searchQueryText: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(searchQueryText))
        when (response.resultCode) {
            ResultCode.SUCCESS_CODE -> {
                val foundTracks = TrackMapper.map((response as TracksSearchResponse).results)
                if (foundTracks.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    val tracksWithFavoriteFlag = getFavoriteTrackIds()
                        .map { favoriteIds ->
                            foundTracks.withFavoriteFlag(favoriteIds)
                        }
                        .map { Resource.Success(it) }
                    emitAll(tracksWithFavoriteFlag)
                }
            }

            ResultCode.CONNECTION_PROBLEM_CODE -> emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.BAD_REQUEST_CODE -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND_CODE -> emit(Resource.Error(ErrorType.NOTHING_FOUND))

            else -> emit(Resource.Error(ErrorType.SERVER_ERROR))
        }
    }

    override suspend fun getSavedHistory(): List<Track> {
        return historyList.withFavoriteFlag(getFavoriteTrackIds().first())
    }

    override suspend fun addTrackToHistory(track: Track) {
        with(historyList) {
            this.removeIf { it.trackId == track.trackId }
            this.add(0, track)
            if (this.size > HISTORY_LIST_MAX_SIZE) {
                this.removeLast()
            }
        }
        localStorage.write(TRACK_HISTORY_LIST, gson.toJson(historyList))
    }

    override fun clearHistory() {
        localStorage.write(TRACK_HISTORY_LIST, "")
        historyList.clear()
    }

    private fun getFavoriteTrackIds(): Flow<List<Long>> {
        return appDatabase.getFavoriteTrackDao().getTrackIds()
    }

    private fun List<Track>.withFavoriteFlag(favoriteTrackIds: List<Long>): List<Track> {
        return this.map { track ->
            track.copy(isFavorite = favoriteTrackIds.contains(track.trackId))
        }
    }

    companion object {
        private const val TRACK_HISTORY_LIST = "track_history_list"
        private const val HISTORY_LIST_MAX_SIZE = 10
    }

}
