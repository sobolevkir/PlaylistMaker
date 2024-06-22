package com.sobolevkir.playlistmaker.search.data.network

import android.content.Context
import com.sobolevkir.playlistmaker.common.ext.isNetworkConnected
import com.sobolevkir.playlistmaker.search.data.dto.NetworkResponse
import com.sobolevkir.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResponse {
        if (!context.isNetworkConnected) {
            return NetworkResponse().apply { resultCode = ResultCode.CONNECTION_PROBLEM_CODE }
        }
        if (dto !is TracksSearchRequest) {
            return NetworkResponse().apply { resultCode = ResultCode.BAD_REQUEST_CODE }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = iTunesApiService.searchTrack(dto.searchQueryText)
                response.apply { resultCode = ResultCode.SUCCESS_CODE }
            } catch (ex: Exception) {
                when (ex) {
                    is retrofit2.HttpException -> {
                        if (ex.code() == ResultCode.NOTHING_FOUND_CODE) {
                            NetworkResponse().apply { resultCode = ResultCode.NOTHING_FOUND_CODE }
                        } else {
                            NetworkResponse().apply {
                                resultCode = ResultCode.CONNECTION_PROBLEM_CODE
                            }
                        }
                    }

                    else -> NetworkResponse().apply {
                        resultCode = ResultCode.CONNECTION_PROBLEM_CODE
                    }
                }

            }
        }
    }
}