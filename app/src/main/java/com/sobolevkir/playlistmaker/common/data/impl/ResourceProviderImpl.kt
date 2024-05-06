package com.sobolevkir.playlistmaker.common.data.impl

import android.content.Context
import com.sobolevkir.playlistmaker.common.domain.ResourceProvider

class ResourceProviderImpl(private val context: Context): ResourceProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}