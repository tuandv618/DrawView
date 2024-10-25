package com.canvas.paint.panda.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val path = Path()
    private val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 30F
    }

    private val pointPaint = Paint().apply {
        color = Color.RED // Màu sắc của chấm tròn
        style = Paint.Style.FILL // Vẽ chấm tròn đầy
    }

    private val points = mutableListOf<PointF>() // Danh sách để lưu trữ các điểm

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                points.add(PointF(x, y)) // Thêm điểm bắt đầu
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                // Thêm điểm vào danh sách với khoảng cách
                if (points.size == 0 || points.last().distance(x, y) > 50) { // Thay đổi khoảng cách nếu cần
                    points.add(PointF(x, y))
                }
                invalidate()
            }
        }
        return true
    }

    private fun PointF.distance(x: Float, y: Float): Float {
        return sqrt(((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y)).toDouble()).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)

        // Vẽ các chấm tròn tại các điểm
        for (point in points) {
            canvas.drawCircle(point.x, point.y, 10f, pointPaint) // Kích thước của chấm tròn
        }
    }
}

