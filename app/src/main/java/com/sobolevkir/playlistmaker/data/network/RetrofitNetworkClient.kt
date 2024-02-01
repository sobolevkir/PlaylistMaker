package com.sobolevkir.playlistmaker.data.network

import com.sobolevkir.playlistmaker.data.dto.NetworkResponse
import com.sobolevkir.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesApi.ITUNES_API_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApiService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): NetworkResponse {
        return if (dto is TracksSearchRequest) {
            val resp = iTunesApiService.searchTrack(dto.searchQueryText).execute()

            val body = resp.body() ?: NetworkResponse()

            body.apply { resultCode = resp.code() }
        } else {
            NetworkResponse().apply { resultCode = 400 }
        }
    }

}