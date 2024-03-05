package com.sobolevkir.playlistmaker.search.data.network

import android.content.Context
import com.sobolevkir.playlistmaker.ext.isNetworkConnected
import com.sobolevkir.playlistmaker.search.data.model.ResultCode

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService,
    private val context: Context
) : NetworkClient {

    override fun doRequest(dto: Any): NetworkResponse {
        try {
            if (!context.isNetworkConnected) {
                return NetworkResponse().apply { resultCode = ResultCode.CONNECTION_PROBLEM_CODE }
            }
            if (dto !is TracksSearchRequest) {
                return NetworkResponse().apply { resultCode = ResultCode.BAD_REQUEST_CODE }
            }

            val resp = iTunesApiService.searchTrack(dto.searchQueryText).execute()
            val body = resp.body() ?: NetworkResponse()
            return body.apply { resultCode = resp.code() }
        } catch (ex: Exception) {
            return NetworkResponse().apply { resultCode = -1 }
        }
    }

}