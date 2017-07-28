package com.oakkub.pointpoker

import android.app.DialogFragment
import android.app.FragmentManager
import java.lang.IllegalStateException

/**
 * Created by oakkub on 7/28/2017 AD.
 */
fun DialogFragment.showIfFailThenAllowStateLoss(fragmentManager: FragmentManager, tag: String? = null) {
    try {
        show(fragmentManager, tag)
    } catch (e: IllegalStateException) {
        fragmentManager.beginTransaction()
                .add(this, tag)
                .commitAllowingStateLoss()
    }
}