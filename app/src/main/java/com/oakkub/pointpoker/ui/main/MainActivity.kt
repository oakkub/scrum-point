package com.oakkub.pointpoker.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import com.oakkub.pointpoker.custom_views.PokerColors
import com.oakkub.pointpoker.custom_views.PokerView
import com.oakkub.pointpoker.extensions.matchWidthMatchHeight
import com.oakkub.pointpoker.extensions.matchWidthWrapHeight
import com.oakkub.pointpoker.extensions.showIfFailThenAllowStateLoss
import com.oakkub.pointpoker.helpers.SelectedColorSharedPreference
import com.oakkub.pointpoker.ui.detail.SelectedPokerDialogFragment
import com.oakkub.pointpoker.ui.settings.color.ChangeColorActivity

class MainActivity : Activity() {

    private val pokerView: PokerView by lazy(LazyThreadSafetyMode.NONE) {
        createPokerView()
    }

    private val colorPrefs: SelectedColorSharedPreference by lazy(LazyThreadSafetyMode.NONE) {
        SelectedColorSharedPreference(this)
    }

    companion object {
        private const val TAG_SELECTED_CARD_DIALOG = "SELECTED_CARD_DIALOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createScrollingContentContainer())
    }

    override fun onResume() {
        super.onResume()

        val (selectedColor, pressedColor) = colorPrefs.getSelectedColor()
        pokerView.selectedColor = PokerColors(selectedColor.toInt(), pressedColor.toInt())
    }

    private fun createScrollingContentContainer(): ScrollView {
        return ScrollView(this).matchWidthMatchHeight().apply {
            id = View.generateViewId()
            addView(pokerView)
        }
    }

    private fun createPokerView(): PokerView {
        val coffeeEmoji = "\u2615"
        val verticalEllipsis = "\u22ee"
        val items = arrayOf("0",
                "1/2",
                "1",
                "2",
                "3",
                "5",
                "8",
                "13",
                "20",
                "40",
                "100",
                "?",
                "∞",
                coffeeEmoji,
                verticalEllipsis)

        return PokerView(this).matchWidthWrapHeight().apply {
            id = View.generateViewId()
            selectedColor = PokerColors(
                    background = colorPrefs.getSelectedColor().color.toInt(),
                    backgroundPressed = colorPrefs.getSelectedColor().pressedColor.toInt(),
                    text = Color.WHITE)
            pokerItems.addAll(items)

            setOnItemSelectedListener {
                if (it == verticalEllipsis) {
                    val intent = Intent(this@MainActivity, ChangeColorActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener
                }

                val dialog = SelectedPokerDialogFragment.create(it)
                dialog.showIfFailThenAllowStateLoss(fragmentManager, TAG_SELECTED_CARD_DIALOG)
            }
        }
    }
}