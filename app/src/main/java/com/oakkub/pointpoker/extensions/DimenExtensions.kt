package com.oakkub.pointpoker.extensions

import android.content.Context
import android.util.TypedValue

/**
 * Created by oakkub on 8/12/2017 AD.
 */
val Int.dp
    get() = this / android.content.res.Resources.getSystem().displayMetrics.density

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