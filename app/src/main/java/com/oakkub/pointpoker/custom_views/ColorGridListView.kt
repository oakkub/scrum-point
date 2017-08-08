package com.oakkub.pointpoker.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.oakkub.pointpoker.helpers.SelectedColor
import com.oakkub.pointpoker.helpers.SelectedColorSharedPreference
import com.oakkub.pointpoker.ui.detail.dp
import kotlin.properties.Delegates

/**
 * Created by oakkub on 8/8/2017 AD.
 */
class ColorGridListView(context: Context,
                        attrs: AttributeSet? = null,
                        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    val blockItemList = mutableListOf<RectF>()
    val colorPrefs: SelectedColorSharedPreference by lazy(LazyThreadSafetyMode.NONE) {
        SelectedColorSharedPreference(context)
    }

    var itemSelectedListener: ((Pair<Long, Long>) -> Unit)? = null
    var selectedBlockColor: RectF? = null

    var checkedColor: SelectedColor by Delegates.vetoable(colorPrefs.getSelectedColor()) {
        _, oldValue, newValue ->
        if (newValue == oldValue) {
            false
        } else {
            invalidate()
            true
        }
    }

    val column: Int by Delegates.observable(3) {
        _, _, _ ->
        requestLayout()
    }

    val colorList: MutableList<Long> by Delegates.observable(mutableListOf()) {
        _, _, _ ->
        requestLayout()
    }

    val colorPressedList: MutableList<Long> by Delegates.observable(mutableListOf()) {
        _, _, _ ->
        requestLayout()
    }

    private val colorBlockPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val colorBlockPressedPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val checkMarkPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 48.dp
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val heightForEachBlock = measuredWidth / column
        val itemReminder = colorList.size % column
        val calculatedTotalHeight = heightForEachBlock * ((colorList.size + itemReminder) / column)

        val totalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(calculatedTotalHeight, MeasureSpec.EXACTLY)
        setMeasuredDimension(widthMeasureSpec, totalHeightMeasureSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val action = event.action

        return when (action) {
            MotionEvent.ACTION_UP -> {
                handleActionUpEvent(x, y)
                true
            }
            MotionEvent.ACTION_DOWN -> {
                handleActionDownEvent(x, y)
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                handleActionCancelEvent()
                true
            }
            else -> {
                false
            }
        }
    }

    private fun handleActionUpEvent(x: Float, y: Float) {
        val actionUpAtIndex: Int = blockItemList
                .indexOfFirst { it.contains(x, y) }
                .takeIf { it in blockItemList.indices } ?: return

        selectedBlockColor?.let {
            redrawSpecificBounds(it)

            if (!it.contains(x, y)) {
                return@let
            }

            val color = colorList[actionUpAtIndex]
            val pressedColor = colorPressedList[actionUpAtIndex]

            colorPrefs.saveSelectedColor(color, pressedColor)
            checkedColor = colorPrefs.getSelectedColor()

            itemSelectedListener?.let {
                it(color to pressedColor)
            }
        }
        selectedBlockColor = null
    }

    private fun handleActionDownEvent(x: Float, y: Float) {
        selectedBlockColor = blockItemList.firstOrNull { it.contains(x, y) }?.apply {
            redrawSpecificBounds(this)
        }
    }

    private fun handleActionCancelEvent() {
        selectedBlockColor?.let {
            redrawSpecificBounds(it)
        }
        selectedBlockColor = null
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val blockWidth: Float = (measuredWidth / column).toFloat()
        val blockHeight: Float = blockWidth
        val colorListSize = colorList.size
        val colorPressedListSize = colorPressedList.size

        if (colorListSize != colorPressedListSize) {
            return
        }

        blockItemList.clear()
        colorList.forEachIndexed { index, value ->
            val color = value
            val pressedColor = colorPressedList[index]

            val currentColumn = index % column
            val currentRow = index / column
            val left = blockWidth * currentColumn
            val top = Math.floor((blockHeight * currentRow).toDouble()).toFloat()
            val right = left + blockWidth
            val bottom = top + blockHeight

            colorBlockPaint.color = color.toInt()
            colorBlockPressedPaint.color = pressedColor.toInt()

            val bounds = RectF(left, top, right, bottom)
            if (isBlockSelected(bounds)) {
                canvas.drawRect(bounds, colorBlockPressedPaint)
            } else {
                canvas.drawRect(bounds, colorBlockPaint)
            }

            if (checkedColor.color == color && checkedColor.pressedColor == pressedColor) {
                drawCheckMark(canvas, bounds, checkMarkPaint)
            }

            blockItemList.add(bounds)
        }
    }

    private fun redrawSpecificBounds(rect: RectF) {
        val dirty = Rect(rect.left.toInt(), rect.top.toInt(), rect.right.toInt(), rect.bottom.toInt())
        invalidate(dirty)
    }

    private fun drawCheckMark(canvas: Canvas, bounds: RectF, checkMarkPaint: Paint) {
        val left = bounds.left
        val top = bounds.top
        val width = bounds.width()
        val height = bounds.height()

        // draw \
        canvas.drawLine(
                left + width / 4f,
                top + height - (height / 2.5f),
                left + width / 2f,
                top + height - (height / 4f),
                checkMarkPaint)

        // draw /
        canvas.drawLine(
                left + width / 2f,
                top + height - (height / 4f),
                left + width - (width / 4f),
                top + height / 4f,
                checkMarkPaint)
    }

    private fun isBlockSelected(bounds: RectF): Boolean = selectedBlockColor?.contains(bounds) ?: false

}