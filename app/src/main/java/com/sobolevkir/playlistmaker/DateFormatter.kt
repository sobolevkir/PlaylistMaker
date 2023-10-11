package com.sobolevkir.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

class DateFormatter {
    fun formatTime(timeMillis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }
}