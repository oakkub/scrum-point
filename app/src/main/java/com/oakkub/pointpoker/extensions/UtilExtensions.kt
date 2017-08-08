package com.oakkub.pointpoker.extensions

import android.content.Context
import android.content.SharedPreferences
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

inline fun fromLolipopOrAbove(fromLolipop: () -> Unit, fromBelow: () -> Unit) {
    if (Build.VERSION.SDK_INT >= 21) {
        fromLolipop()
    } else {
        fromBelow()
    }
}

fun Context.getCompatColor(id: Int): Int = if (Build.VERSION.SDK_INT >= 23) {
    getColor(id)
} else {
    @Suppress("DEPRECATION")
    resources.getColor(id)
}

inline fun SharedPreferences.editor(action: SharedPreferences.Editor.() -> Unit) {
    val edit = edit()
    edit.action()
    edit.apply()
}
