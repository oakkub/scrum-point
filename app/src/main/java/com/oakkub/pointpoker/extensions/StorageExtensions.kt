package com.oakkub.pointpoker.extensions

import android.content.SharedPreferences

/**
 * Created by oakkub on 8/12/2017 AD.
 */
inline fun SharedPreferences.editor(action: SharedPreferences.Editor.() -> Unit) {
    val edit = edit()
    edit.action()
    edit.apply()
}
