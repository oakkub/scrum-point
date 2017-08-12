package com.oakkub.pointpoker.extensions

import android.content.Context
import android.os.Build

/**
 * Created by oakkub on 8/12/2017 AD.
 */
fun Context.getCompatColor(id: Int): Int = if (Build.VERSION.SDK_INT >= 23) {
    getColor(id)
} else {
    @Suppress("DEPRECATION")
    resources.getColor(id)
}