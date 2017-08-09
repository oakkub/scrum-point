package com.oakkub.pointpoker.ui.settings.color

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import com.oakkub.pointpoker.custom_views.ColorGridListView
import com.oakkub.pointpoker.custom_views.CustomToolbar
import com.oakkub.pointpoker.extensions.matchWidthMatchHeight
import com.oakkub.pointpoker.extensions.matchWidthWrapHeight
import com.oakkub.simplepoker.R

/**
 * Created by oakkub on 8/8/2017 AD.
 */
class ChangeColorActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).matchWidthMatchHeight().apply {
            orientation = LinearLayout.VERTICAL

            val toolbar = CustomToolbar(context).matchWidthWrapHeight().apply {
                title = getString(R.string.change_color_title)
                onBackIconClickListener = {
                    finish()
                }
            }
            val container = createScrollingContainer()

            addView(toolbar)
            addView(container)
        }

        setContentView(root)
    }

    fun createScrollingContainer() = ScrollView(this).matchWidthMatchHeight().apply {
        addView(createContainer())
    }

    fun createContainer() = LinearLayout(this).matchWidthWrapHeight().apply {
        addView(createColorGridView())
    }

    fun createColorGridView() = ColorGridListView(this).matchWidthWrapHeight().apply {
        val preparedColorList = arrayOf(
                0xFF1DE9B6, // Default color (Cannot use color resource)
                0xFFEE0000,
                0xFFE91E63,
                0xFF9C27B0,
                0xFF673AB7,
                0xFF3F51B5,
                0xFF1976D2,
                0xFF03A9F4,
                0xFF00BCD4,
                0xFF009688,
                0xFF4CAF50,
                0xFF8BC34A,
                0xFFCDDC39,
                0xFFFFFF00,
                0xFFFFC107,
                0xFFFF9800,
                0xFFFF5722,
                0xFF795548,
                0xFF9E9E9E,
                0xFF607D8B,
                0xFF37474F)

        val preparedColorPressedList = arrayOf(
                0xFF00BFA5, // Default pressed color (Cannot use color resource)
                0xFFD50000,
                0xFFC2185B,
                0xFF7B1FA2,
                0xFF512DA8,
                0xFF303F9F,
                0xFF1565C0,
                0xFF0288D1,
                0xFF00ACC1,
                0xFF00796B,
                0xFF388E3C,
                0xFF689F38,
                0xFFAFB42B,
                0xFFFFE800,
                0xFFFFB300,
                0xFFFB8C00,
                0xFFE64A19,
                0xFF5D4037,
                0xFF757575,
                0xFF455A64,
                0xFF263238)

        colorList.addAll(preparedColorList)
        colorPressedList.addAll(preparedColorPressedList)
    }

}