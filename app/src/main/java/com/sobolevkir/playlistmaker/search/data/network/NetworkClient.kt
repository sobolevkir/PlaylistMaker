package com.sobolevkir.playlistmaker.search.data.network

import com.sobolevkir.playlistmaker.search.data.dto.NetworkResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): NetworkResponse
}