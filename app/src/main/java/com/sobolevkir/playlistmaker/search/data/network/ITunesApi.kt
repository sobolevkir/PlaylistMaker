package com.sobolevkir.playlistmaker.search.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    companion object {
        const val ITUNES_API_BASEURL = "https://itunes.apple.com"
    }

    @GET("/search?entity=song")
    fun searchTrack(@Query("term") searchQueryText: String): Call<TracksSearchResponse>

}