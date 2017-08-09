package com.oakkub.pointpoker.ui.main

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.oakkub.pointpoker.custom_views.PokerColors
import com.oakkub.pointpoker.custom_views.PokerView
import com.oakkub.pointpoker.extensions.*
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
        private const val SETTINGS_REQUEST_CODE = 100
        private const val TAG_SELECTED_CARD_DIALOG = "SELECTED_CARD_DIALOG"
    }

    private val scrollableContainerView: ScrollView by lazy(LazyThreadSafetyMode.NONE) {
        createScrollingContentContainer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(scrollableContainerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SETTINGS_REQUEST_CODE) {
            val selectedColor = colorPrefs.getSelectedColor()
            pokerView.selectedColor = PokerColors(selectedColor.color.toInt(), selectedColor.pressedColor.toInt())
        }
    }

    private fun createScrollingContentContainer(): ScrollView {
        return ScrollView(this).matchWidthMatchHeight().apply {
            id = View.generateViewId()

            fromLolipopOrAbove {
                applyTranslucentContent(this)
                applyViewPaddingWhenContentIsTranslucent(this)
            }

            addView(pokerView)
        }
    }

    private fun applyTranslucentContent(view: View) {
        view.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private fun applyViewPaddingWhenContentIsTranslucent(container: ViewGroup) {
        container.setOnApplyWindowInsetsListener { _, windowInsets ->
            container.apply {
                clipToPadding = false
                setPadding(
                        windowInsets.systemWindowInsetLeft,
                        windowInsets.systemWindowInsetTop,
                        windowInsets.systemWindowInsetRight,
                        windowInsets.systemWindowInsetBottom)
                setOnApplyWindowInsetsListener(null)
            }
            windowInsets.consumeSystemWindowInsets()
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
                    startActivityForResult(intent, SETTINGS_REQUEST_CODE)
                    return@setOnItemSelectedListener
                }

                val dialog = SelectedPokerDialogFragment.create(it)
                dialog.showIfFailThenAllowStateLoss(fragmentManager, TAG_SELECTED_CARD_DIALOG)
            }
        }
    }
}