package com.oakkub.pointpoker.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.os.Build


/**
 * Created by oakkub on 6/7/2017 AD.
 */

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

fun <V: View> V.wrapWidthWrapHeight(): V {
    val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutParams = params
    return this
}