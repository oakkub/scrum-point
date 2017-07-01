package com.oakkub.simplepoker

import android.app.DialogFragment
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView


/**
 * Created by oakkub on 6/7/2017 AD.
 */
class SelectedPokerDialogFragment : DialogFragment() {

    companion object {
        private val ARGS_TEXT = "TEXT"

        fun create(text: String): SelectedPokerDialogFragment {
            val args = Bundle()
            args.putString(ARGS_TEXT, text)

            val selectedPokerDialog = SelectedPokerDialogFragment()
            selectedPokerDialog.arguments = args
            return selectedPokerDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogBlackColorTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_selected_poker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = arguments.getString(ARGS_TEXT)

        val itemTextView: TextView = view.findViewById(R.id.itemTextView)
        itemTextView.text = text
        itemTextView.setOnClickListener {
            dismissAllowingStateLoss()
        }

        setRoundedBackground(itemTextView, 0xFF1DE9B6.toInt(), 0xFFFFFFFF.toInt())
    }

    override fun onResume() {
        val windowParams = dialog.window.attributes
        windowParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        windowParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.attributes = windowParams

        super.onResume()
    }

    private fun setRoundedBackground(view: View, backgroundColor: Int, borderColor: Int) {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 64.dp
        shape.setColor(backgroundColor)
        shape.setStroke(resources.getDimensionPixelSize(R.dimen.poker_selected_item_border_size), borderColor)
        view.background = shape
    }

}

val Int.dp
    get() = this / android.content.res.Resources.getSystem().displayMetrics.density
