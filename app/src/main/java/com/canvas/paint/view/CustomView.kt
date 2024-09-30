package com.canvas.paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val path = Path()
    private var controlPointY = 200f // Độ cong điều chỉnh

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Đặt lại Path
        path.reset()

        // Vẽ đường cong từ (100, 500) đến (600, 500)
        path.moveTo(100f, 500f)
        path.quadTo(350f, controlPointY, 600f, 500f)

        // Vẽ đường cong trên Canvas
        canvas.drawPath(path, paint)
    }

    fun updateControlPoint(y: Float) {
        controlPointY = y // Cập nhật vị trí điểm điều khiển
        invalidate() // Yêu cầu vẽ lại
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 800 // Chiều rộng tối đa của CustomView
        val desiredHeight = 800 // Chiều cao tối đa của CustomView

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> desiredWidth.coerceAtMost(widthSize)
            else -> desiredWidth
        }

        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> desiredHeight.coerceAtMost(heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height)
    }
}
