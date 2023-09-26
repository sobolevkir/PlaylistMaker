package com.sobolevkir.playlistmaker

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    internal fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    internal fun formatTrackTime(trackTimeMillis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }

}