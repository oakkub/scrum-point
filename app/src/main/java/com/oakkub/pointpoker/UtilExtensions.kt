package com.oakkub.pointpoker

import android.os.Build
import android.util.Log

/**
 * Created by oakkub on 8/7/2017 AD.
 */
infix fun String.logDebug(message: String) {
    Log.d(this, message)
}

inline fun fromLolipopOrAbove(action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= 21) {
        action()
    }
}