package com.sobolevkir.playlistmaker.ext

import android.content.Context
import android.content.res.Configuration

fun Context.isNightModeOn(): Boolean =
    resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES