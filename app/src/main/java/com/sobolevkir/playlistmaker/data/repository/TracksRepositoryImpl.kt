package com.sobolevkir.playlistmaker.data.repository

import com.sobolevkir.playlistmaker.data.dto.TracksSearchRequest
import com.sobolevkir.playlistmaker.data.dto.TracksSearchResponse
import com.sobolevkir.playlistmaker.data.mapper.TrackMapper
import com.sobolevkir.playlistmaker.data.network.NetworkClient
import com.sobolevkir.playlistmaker.domain.api.TracksRepository
import com.sobolevkir.playlistmaker.domain.models.Track


class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTrack(searchQueryText: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(searchQueryText))
        return if (response.resultCode == 200) {
            TrackMapper.map((response as TracksSearchResponse).results)
        } else {
            emptyList()
        }
    }
}
