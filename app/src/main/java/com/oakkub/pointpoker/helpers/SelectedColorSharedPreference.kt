package com.oakkub.pointpoker.helpers

import android.content.Context
import com.oakkub.pointpoker.extensions.editor

/**
 * Created by oakkub on 8/8/2017 AD.
 */
data class SelectedColor(val color: Long, val pressedColor: Long)

class SelectedColorSharedPreference(val context: Context) {

    companion object {
        const val SELECTED_COLOR_PREF_FILE_NAME = "selectedColor"
        const val SELECTED_COLOR_KEY = "color"
        const val SELECTED_COLOR_PRESSED_KEY = "pressedColor"

        const val DEFAULT_COLOR: Long = 0xFF1DE9B6
        const val DEFAULT_PRESSED_COLOR: Long = 0xFF00BFA5
    }

    private val prefs = context.getSharedPreferences(SELECTED_COLOR_PREF_FILE_NAME, Context.MODE_PRIVATE)

    fun saveSelectedColor(color: Long, pressedColor: Long) {
        prefs.editor {
            putLong(SELECTED_COLOR_KEY, color)
            putLong(SELECTED_COLOR_PRESSED_KEY, pressedColor)
        }
    }

    fun getSelectedColor(): SelectedColor {
        val color = prefs.getLong(SELECTED_COLOR_KEY, DEFAULT_COLOR)
        val pressedColor = prefs.getLong(SELECTED_COLOR_PRESSED_KEY, DEFAULT_PRESSED_COLOR)

        return SelectedColor(color, pressedColor)
    }

}