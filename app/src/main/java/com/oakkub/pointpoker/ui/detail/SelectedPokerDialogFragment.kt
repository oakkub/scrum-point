package com.oakkub.pointpoker.ui.detail

import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.oakkub.pointpoker.extensions.dismissIfFailThenAllowStateLoss
import com.oakkub.pointpoker.extensions.dp
import com.oakkub.pointpoker.extensions.setTextSizeInPixel
import com.oakkub.pointpoker.helpers.SelectedColorSharedPreference
import com.oakkub.pointpoker.helpers.ShakeDetector
import com.oakkub.simplepoker.R


/**
 * Created by oakkub on 6/7/2017 AD.
 */
class SelectedPokerDialogFragment : DialogFragment() {

    companion object {
        private const val ARGS_TEXT = "TEXT"
        private const val STATE_DID_USER_SHAKE_DEVICE = "DID_USER_SHAKE_DEVICE"

        fun create(text: String): SelectedPokerDialogFragment {
            val args = Bundle()
            args.putString(ARGS_TEXT, text)

            val selectedPokerDialog = SelectedPokerDialogFragment()
            selectedPokerDialog.arguments = args
            return selectedPokerDialog
        }
    }

    private lateinit var pointDetailContainer: FrameLayout
    private lateinit var pointDetailTextView: TextView
    private lateinit var pointDetailMessageTextView: TextView
    private var didUserShakeDevice = false

    private val shakeDetector: ShakeDetector by lazy(LazyThreadSafetyMode.NONE) {
        // We need to wait for the activity to be initialized
        // So we use lazy since we will call this variable later in the appropriate lifecycle anyway
        ShakeDetector(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogBlackColorTheme)

        savedInstanceState?.let {
            didUserShakeDevice = it.getBoolean(STATE_DID_USER_SHAKE_DEVICE, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_selected_poker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance(view)
        initShakeDetector()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_DID_USER_SHAKE_DEVICE, didUserShakeDevice)
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.resume()
    }

    override fun onPause() {
        shakeDetector.pause()
        super.onPause()
    }

    private fun initInstance(view: View) {
        pointDetailContainer = view.findViewById(R.id.pointDetailContainer)
        pointDetailTextView = view.findViewById(R.id.pointDetailTextView)
        pointDetailMessageTextView = view.findViewById(R.id.pointDetailMessageTextView)
        showPointDetail(didUserShakeDevice)

        val selectedColor = SelectedColorSharedPreference(activity).getSelectedColor().color.toInt()
        setRoundedBackground(pointDetailContainer,
                backgroundColor = selectedColor,
                borderColor = Color.WHITE)
    }

    private fun initShakeDetector() {
        shakeDetector.onShakeListener = {
            if (it == 1) {
                didUserShakeDevice = true
                showPointDetail(didUserShakeDevice)
            }
        }
    }

    private fun setRoundedBackground(view: View, backgroundColor: Int, borderColor: Int) {
        val gradientDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 64.dp
            setColor(backgroundColor)
            setStroke(resources.getDimensionPixelSize(R.dimen.poker_selected_item_border_size), borderColor)
        }
        view.background = gradientDrawable
    }

    private fun showPointDetail(didUserShakeDevice: Boolean) {
        TransitionManager.beginDelayedTransition(pointDetailContainer)

        if (didUserShakeDevice) {
            pointDetailTextView.apply {
                text = arguments.getString(ARGS_TEXT)
                setTextSizeInPixel(resources.getDimension(R.dimen.poker_selected_item_point_text_size))
                setOnClickListener {
                    dismissIfFailThenAllowStateLoss()
                }
            }
            pointDetailMessageTextView.text = getString(R.string.point_detail_touch_to_close)
        } else {
            pointDetailTextView.apply {
                text = getString(R.string.point_detail_ready_state)
                setTextSizeInPixel(resources.getDimension(R.dimen.poker_selected_item_ready_point_text_size))
                setOnClickListener {
                    this@SelectedPokerDialogFragment.didUserShakeDevice = true
                    showPointDetail(this@SelectedPokerDialogFragment.didUserShakeDevice)
                }
            }
            pointDetailMessageTextView.text = getString(R.string.point_detail_touch_to_show_point)
        }
    }

}