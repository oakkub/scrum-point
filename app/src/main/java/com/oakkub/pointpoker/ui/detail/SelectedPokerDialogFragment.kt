package com.oakkub.pointpoker.ui.detail

import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.oakkub.pointpoker.extensions.dismissIfFailThenAllowStateLoss
import com.oakkub.pointpoker.extensions.dp
import com.oakkub.simplepoker.R
import com.oakkub.pointpoker.helpers.SelectedColorSharedPreference


/**
 * Created by oakkub on 6/7/2017 AD.
 */
class SelectedPokerDialogFragment : DialogFragment() {

    companion object {
        private const val ARGS_TEXT = "TEXT"

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
        val pokerText = arguments.getString(ARGS_TEXT)

        val itemTextView: TextView = view.findViewById(R.id.itemTextView)
        itemTextView.apply {
            text = pokerText
            setOnClickListener {
                dismissIfFailThenAllowStateLoss()
            }
        }

        setRoundedBackground(itemTextView,
                backgroundColor = SelectedColorSharedPreference(activity).getSelectedColor().color.toInt(),
                borderColor = Color.WHITE)
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