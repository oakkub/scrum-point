package com.oakkub.pointpoker

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.oakkub.simplepoker.*

class MainActivity : Activity() {

    private val scrollableContainerView: ScrollView by lazy(LazyThreadSafetyMode.NONE) {
        createScrollingContentContainer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(scrollableContainerView)
    }

    private fun createScrollingContentContainer(): ScrollView {
        return ScrollView(this).fullExpand().apply {
            id = View.generateViewId()

            fromLolipopOrAbove {
                applyTranslucentContent(this)
                applyViewPaddingWhenContentIsTranslucent(this)
            }

            plus(createPokerView())
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
        val items = arrayOf("0", "1/2", "1", "2", "3", "5", "8", "13", "20", "40", "100", "?", "∞", "\u2615")

        return PokerView(this).fullWidthWrapHeight().apply {
            id = View.generateViewId()
            selectedColor = PokerColors(
                    background = getCompatColor(R.color.colorPrimary),
                    backgroundPressed = getCompatColor(R.color.colorPrimaryDark),
                    text = Color.WHITE)
            pokerItems.addAll(items)

            setOnItemSelectedListener {
                val dialog = SelectedPokerDialogFragment.create(it)
                dialog.showIfFailThenAllowStateLoss(fragmentManager, "selectedPoker")
            }
        }
    }
}