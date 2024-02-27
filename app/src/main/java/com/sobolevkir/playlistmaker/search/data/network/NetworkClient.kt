package com.sobolevkir.playlistmaker.search.data.network

interface NetworkClient {
    fun doRequest(dto: Any): NetworkResponse
}