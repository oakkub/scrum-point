package com.oakkub.simplepoker

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.os.Build


/**
 * Created by oakkub on 6/7/2017 AD.
 */

operator fun ViewGroup.plus(view: View) {
    addView(view)
}

fun Context.getCompatColor(id: Int): Int = if (Build.VERSION.SDK_INT >= 23) {
    getColor(id)
} else {
    resources.getColor(id)
}

fun <V : View> V.fullExpand(): V {
    val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
    )
    layoutParams = params
    return this
}

fun <V : View> V.fullWidthWrapHeight(): V {
    val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutParams = params
    return this
}