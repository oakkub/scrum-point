package com.oakkub.pointpoker.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.util.TypedValue

/**
 * Created by oakkub on 8/7/2017 AD.
 */
val Int.dp
    get() = this / android.content.res.Resources.getSystem().displayMetrics.density

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

fun Context.getActionBarSize(): Int {
    val typedValue = TypedValue()
    val isResolved = theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)

    return if (isResolved) {
        TypedValue.complexToDimensionPixelSize(typedValue.data, resources.displayMetrics)
    } else {
        // Default action bar size of mobile phone
        56.dp.toInt()
    }
}

fun Context.getSelectableItemBackgroundResource(): Int {
    val typedValue = TypedValue()
    val isResolved = theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)

    return if (isResolved) {
        typedValue.resourceId
    } else {
        0
    }
}

inline fun SharedPreferences.editor(action: SharedPreferences.Editor.() -> Unit) {
    val edit = edit()
    edit.action()
    edit.apply()
}
