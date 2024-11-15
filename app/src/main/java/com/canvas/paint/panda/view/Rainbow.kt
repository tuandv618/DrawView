package com.canvas.paint.panda.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class Rainbow(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val path = Path()

    // Mảng màu cầu vồng
    private val rainbowColors = listOf(
        Color.RED,
        Color.parseColor("#FFA500"), // Orange
        Color.YELLOW,
        Color.GREEN,
        Color.BLUE,
        Color.parseColor("#4B0082"), // Indigo
        Color.parseColor("#EE82EE")  // Violet
    )

    // Tạo danh sách Paint với các màu và kích thước chia đều nhau
    private val rainbowPaints = rainbowColors.mapIndexed { index, color ->
        Paint().apply {
            this.color = color
            style = Paint.Style.STROKE
            strokeWidth = 70f - index * 10f
            isAntiAlias = true
        }
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null) // Để hỗ trợ hiệu ứng mượt mà hơn
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Vẽ từng đường với các Paint màu cầu vồng theo thứ tự
        rainbowPaints.forEach { paint ->
            canvas.drawPath(path, paint)
        }
    }
}

