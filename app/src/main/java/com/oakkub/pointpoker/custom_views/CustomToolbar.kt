package com.oakkub.pointpoker.custom_views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.oakkub.pointpoker.extensions.*
import com.oakkub.simplepoker.R
import kotlin.properties.Delegates

/**
 * Created by oakkub on 8/9/2017 AD.
 */
class CustomToolbar(context: Context,
                    attributeSet: AttributeSet? = null,
                    defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr) {

    private val backIconTextView: TextView = TextView(context).apply {
        text = "<"
        textSize = 36f
        gravity = Gravity.CENTER
        minimumWidth = resources.getDimensionPixelSize(R.dimen.toolbar_icon_min_size)

        setTextColor(Color.BLACK)
        setBackgroundResource(context.getSelectableItemBackgroundResource())

        setOnClickListener {
            onBackIconClickListener?.invoke(it)
        }
    }
    private val titleTextView: TextView = TextView(context).apply {
        text = ""
        textSize = 24f
        gravity = Gravity.START or Gravity.CENTER_VERTICAL

        setTextColor(Color.BLACK)
    }

    var title: String by Delegates.observable("") {
        _, _, newValue ->
        titleTextView.text = newValue
    }

    var onBackIconClickListener: ((view: View) -> Unit)? = null

    init {
        fromLolipopOrAbove {
            elevation = 4.dp
        }
        orientation = HORIZONTAL
        minimumHeight = context.getActionBarSize()
        gravity = Gravity.CENTER_VERTICAL

        addView(backIconTextView)
        addView(titleTextView, getWeightLayoutParams(1f))
    }

    private fun getWeightLayoutParams(weight: Float) = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weight)

}