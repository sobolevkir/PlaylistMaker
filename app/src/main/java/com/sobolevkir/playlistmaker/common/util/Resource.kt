package com.sobolevkir.playlistmaker.common.util

import com.sobolevkir.playlistmaker.common.domain.model.ErrorType

sealed class Resource<T>(val data: T? = null, val errorType: ErrorType? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorType: ErrorType, data: T? = null) : Resource<T>(data, errorType)
}
