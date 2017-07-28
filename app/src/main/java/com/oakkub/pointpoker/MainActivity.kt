package com.oakkub.simplepoker

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ScrollView
import com.oakkub.pointpoker.showIfFailThenAllowStateLoss

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBarColor()

        val scrollableContainerView = createContainerScrollView()
        scrollableContainerView.id = View.generateViewId()
        scrollableContainerView + createPokerView()

        super.onCreate(savedInstanceState)
        setContentView(scrollableContainerView)
    }

    private fun createPokerView(): PokerView {
        val pokerItems = arrayOf("0", "1/2", "1", "2", "3", "5", "8", "13", "20", "40", "100", "?", "∞", "\u2615")

        val pokerView = PokerView(this).fullWidthWrapHeight()
        pokerView.id = View.generateViewId()

        pokerView.selectedColor = PokerColors(
                background = 0xFF1DE9B6.toInt(),
                backgroundPressed = 0xFF00BFA5.toInt(),
                text = Color.WHITE)

        pokerView.pokerItems.addAll(pokerItems)
        pokerView.setOnItemSelectedListener {
            val dialog = SelectedPokerDialogFragment.create(it)
            dialog.showIfFailThenAllowStateLoss(fragmentManager, "selectedPoker")
        }

        return pokerView
    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0xFF00BFA5.toInt()
        }
    }

    private fun createContainerScrollView() = ScrollView(this).fullExpand().apply {
        setBackgroundColor(0xFFFFFFFF.toInt())
    }

}