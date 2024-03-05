package com.sobolevkir.playlistmaker.common.data.impl

import android.app.Application
import com.sobolevkir.playlistmaker.common.domain.ResourceProvider

class ResourceProviderImpl(private val application: Application): ResourceProvider {
    override fun getString(resId: Int): String {
        return application.getString(resId)
    }
}