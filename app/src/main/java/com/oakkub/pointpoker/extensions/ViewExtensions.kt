package com.oakkub.pointpoker.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.os.Build
import android.util.TypedValue
import android.widget.TextView


/**
 * Created by oakkub on 6/7/2017 AD.
 */

fun <V : View> V.matchWidthMatchHeight(): V {
    val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
    )
    layoutParams = params
    return this
}

fun <V : View> V.matchWidthWrapHeight(): V {
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

fun View.setAllPadding(padding: Int) {
    setPaddingRelative(padding, padding, padding, padding)
}

fun TextView.setTextSizeInPixel(px: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, px)
}