package com.sobolevkir.playlistmaker.search.data.network

import android.content.Context
import com.sobolevkir.playlistmaker.ext.isNetworkConnected
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesApi.ITUNES_API_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApiService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): NetworkResponse {
        try {
            if (!context.isNetworkConnected) {
                return NetworkResponse().apply { resultCode = -1 }
            }
            if (dto !is TracksSearchRequest) {
                return NetworkResponse().apply { resultCode = 400 }
            }

            val resp = iTunesApiService.searchTrack(dto.searchQueryText).execute()
            val body = resp.body() ?: NetworkResponse()
            return body.apply { resultCode = resp.code() }
        } catch (ex: Exception) {
            return NetworkResponse().apply { resultCode = -1 }
        }
    }

    /*private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }

        return false
    }*/

}