package com.canvas.paint.view

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View


class CircleTextView : View {
    private var textPaint: TextPaint? = null
    private var circlePaint: Paint? = null
    private var circleBitmap: Bitmap? = null
    private val bitmapFactory: BitmapFactory? = null
    private var bitmapRect: Rect? = null
    private var viewWidth = 0
    private var viewHeight = 0
    private var text = "button"

    // Views values
    private var circleRadius = 0
    private var circleX = 0
    private var circleY = 0
    private var textSize = 0f
    private var textX = 0
    private var textY = 0

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initTools()
    }

    constructor(context: Context?) : super(context) {
        initTools()
    }

    private fun initTools() {
        textPaint = TextPaint()
        textPaint!!.isAntiAlias = true
        textPaint!!.color = Color.BLACK
        textPaint!!.textSize = 20f
        circlePaint = Paint()
        circlePaint!!.isAntiAlias = true
        circlePaint!!.color = Color.BLUE
        circlePaint!!.style = Paint.Style.FILL
        circleBitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_input_add
        )
    }

    fun setText(text: String) {
        this.text = text
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewidth = resolveSize(0, widthMeasureSpec)
        val viewHeight = resolveSize(0, heightMeasureSpec)
        val minValue = Math.min(viewidth, viewHeight)
        setMeasuredDimension(minValue, minValue)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(
            circleX.toFloat(), circleY.toFloat(), circleRadius.toFloat(),
            circlePaint!!
        )
        canvas.drawBitmap(circleBitmap!!, null, bitmapRect!!, null)
        canvas.drawText(text, textX.toFloat(), textY.toFloat(), textPaint!!)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        circleRadius = viewWidth / 2
        circleX = viewWidth / 2
        circleY = viewHeight / 2
        val halfRadius = circleRadius / 2
        bitmapRect = Rect(
            halfRadius, halfRadius, circleX + halfRadius,
            circleX
        )
        textSize = textPaint!!.measureText(text)
        textX = circleX - textSize.toInt() / 2
        textY = circleY + 20
    }
}



