package com.canvas.paint.panda.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.canvas.paint.R

class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val path = Path()
    private val paint = Paint()

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
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Vẽ đường đã tạo
        canvas.drawPath(path, paint)
    }

    private fun applyPatternedBrush() {
        paint.apply {
            color = Color.BLACK // Màu của đường vẽ
            style = Paint.Style.STROKE
            strokeWidth = 30F // Đặt stroke width khớp với kích thước của bitmap
        }
    }
}
