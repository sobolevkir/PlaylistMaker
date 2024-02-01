package com.sobolevkir.playlistmaker.data.network

import com.sobolevkir.playlistmaker.data.dto.NetworkResponse

interface NetworkClient {
    fun doRequest(dto: Any): NetworkResponse
}