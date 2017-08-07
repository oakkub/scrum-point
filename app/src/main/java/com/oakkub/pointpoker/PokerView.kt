package com.oakkub.pointpoker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.oakkub.simplepoker.R
import kotlin.properties.Delegates

/**
 * Created by oakkub on 6/6/2017 AD.
 */


data class PokerColors(val background: Int = 0xFF000000.toInt(),
                       val backgroundPressed: Int = 0xFF000000.toInt(),
                       val text: Int = 0xFFFFFFFF.toInt())

class PokerView @JvmOverloads constructor(context: Context,
                                          attrs: AttributeSet? = null,
                                          defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    val column: Int by Delegates.observable(3) {
        _, _, _ ->
        requestLayout()
    }

    val space: Float by Delegates.observable(resources.getDimension(R.dimen.poker_item_border_size)) {
        _, _, _ ->
        requestLayout()
    }

    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val pressedBackgroundPaint = Paint(backgroundPaint)

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = resources.getDimension(R.dimen.poker_item_text_size)
    }

    private var pokerBoundsItems = mutableListOf<RectF>()
    private var selectedBoundsItem: RectF? = null

    var pokerItems: ArrayList<String> by Delegates.observable(ArrayList<String>()) {
        _, _, _ ->
        // Request layout to be measured and redraw every time item changes.
        requestLayout()
    }

    var selectedColor by Delegates.observable(PokerColors()) {
        _, _, (background, backgroundPressed, text) ->
        backgroundPaint.color = background
        pressedBackgroundPaint.color = backgroundPressed
        textPaint.color = text
        invalidate()
    }

    var itemSelectedListener: ((String) -> Unit)? = null

    fun setOnItemSelectedListener(itemSelectedListener: (String) -> Unit) {
        this.itemSelectedListener = itemSelectedListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)

        val pokerItemCountReminder = pokerItems.size % column
        val offsetSpace = space
        val newHeight = ((measuredWidth * 1.4f) / column) * ((pokerItems.size + pokerItemCountReminder) / column) - offsetSpace.toInt()

        val newMeasureSpecHeight = MeasureSpec.makeMeasureSpec(newHeight.toInt(), MeasureSpec.EXACTLY)
        setMeasuredDimension(widthMeasureSpec, newMeasureSpecHeight)
    }

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

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val widthMinusBorderSpace = measuredWidth - ((space * (column - 1)))
        val cardWidth: Float = widthMinusBorderSpace / column
        val cardHeight: Float = cardWidth * 1.4f

        var left = 0f
        var top = 0f
        var right = 0f
        var bottom = 0f

        pokerBoundsItems.clear()
        pokerItems.forEachIndexed { index, text ->

            if (index == 0) {
                right = cardWidth
                bottom = cardHeight
            } else if (index % column == 0) {
                left = 0f
                top += cardHeight
                right = cardWidth
                bottom += cardHeight
            } else {
                left += cardWidth
                right += cardWidth
            }

            val currentRow: Float = Math.ceil((index / column).toDouble()).toFloat()
            val currentColumn: Float = (index % column).toFloat()

            val yOffset = if (index >= column) space * currentRow else 0f
            val xOffset = space * currentColumn

            val bounds = RectF(left, top, right, bottom)

            // Add xOffset and yOffset to make space for the border
            bounds.offset(xOffset, yOffset)

            if (isItemSelected(bounds)) {
                canvas.drawRect(bounds, pressedBackgroundPaint)
            } else {
                canvas.drawRect(bounds, backgroundPaint)
            }

            val start = 0
            val end = text.length
            val textX = bounds.left + (cardWidth / 2f) + text.middleX(textPaint)
            val textY = bounds.top + (cardHeight / 2f) + text.middleY(textPaint)
            canvas.drawText(text, start, end, textX, textY, textPaint)

            pokerBoundsItems.add(bounds)
        }
    }

    private fun handleActionUpEvent(x: Float, y: Float) {
        val actionUpAtIndex: Int = pokerBoundsItems.indexOfFirst { it.contains(x, y) }
                .takeIf { it in pokerBoundsItems.indices } ?: return

        selectedBoundsItem?.let {
            redrawSpecificBounds(it)

            if (it.contains(x, y)) {
                itemSelectedListener?.let {
                    it(pokerItems[actionUpAtIndex])
                }
            }
        }
        selectedBoundsItem = null
    }

    private fun handleActionDownEvent(x: Float, y: Float) {
        selectedBoundsItem = pokerBoundsItems.firstOrNull { it.contains(x, y) }?.apply {
            redrawSpecificBounds(this)
        }
    }

    private fun handleActionCancelEvent() {
        selectedBoundsItem?.let {
            redrawSpecificBounds(it)
        }
        selectedBoundsItem = null
    }

    private fun redrawSpecificBounds(rect: RectF) {
        val dirty = Rect(rect.left.toInt(), rect.top.toInt(), rect.right.toInt(), rect.bottom.toInt())
        invalidate(dirty)
    }

    private fun isItemSelected(bounds: RectF) = selectedBoundsItem?.contains(bounds) ?: false

    private fun String.middleX(paint: Paint, start: Int = 0, end: Int = length): Float {
        val bounds = getTextBounds(this, start, end, paint)
        return bounds.width() / 2f - bounds.right
    }

    private fun String.middleY(paint: Paint, start: Int = 0, end: Int = length): Float {
        val bounds = getTextBounds(this, start, end, paint)
        return bounds.height() / 2f - bounds.bottom
    }

    private fun getTextBounds(text: String, start: Int = 0, end: Int = text.length, paint: Paint): Rect = Rect().apply {
        paint.getTextBounds(text, start, end, this)
    }

}
