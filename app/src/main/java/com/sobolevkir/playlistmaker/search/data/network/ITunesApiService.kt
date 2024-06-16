package com.sobolevkir.playlistmaker.search.data.network

import com.sobolevkir.playlistmaker.search.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    companion object {
        const val ITUNES_API_BASEURL = "https://itunes.apple.com"
    }

    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") searchQueryText: String): TracksSearchResponse

}