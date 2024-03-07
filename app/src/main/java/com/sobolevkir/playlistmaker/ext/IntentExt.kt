package com.sobolevkir.playlistmaker.ext

import android.content.Intent
import android.os.Build
import java.io.Serializable

/*fun Intent.getSerializableExtraCompat(key: String, clazz: Class<out Serializable>): Serializable? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, clazz::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(key)
    }
}*/

inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(key: String): T? {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
            getSerializableExtra(key, T::class.java)

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}