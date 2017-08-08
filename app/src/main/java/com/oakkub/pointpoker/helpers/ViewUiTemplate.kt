package com.oakkub.pointpoker.helpers

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import com.oakkub.pointpoker.extensions.fullWidthWrapHeight
import com.oakkub.simplepoker.R

/**
 * Created by oakkub on 8/8/2017 AD.
 */
fun createToolbarTextView(context: Context) = TextView(context).fullWidthWrapHeight().apply {
    textSize = resources.getDimension(R.dimen.toolbar_text_size)
    gravity = Gravity.CENTER
    setTextColor(Color.BLACK)
}