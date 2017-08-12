package com.oakkub.pointpoker.extensions

import android.content.Context
import android.util.TypedValue

/**
 * Created by oakkub on 8/12/2017 AD.
 */
fun Context.getSelectableItemBackgroundResource(): Int {
    val typedValue = TypedValue()
    val isResolved = theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)

    return if (isResolved) {
        typedValue.resourceId
    } else {
        0
    }
}