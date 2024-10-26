package com.canvas.paint.panda.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.canvas.paint.panda.model.Line

class TapeView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 30F
        isAntiAlias = true
    }

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    // Danh sách lưu trữ các đường thẳng đã vẽ
    private val lines = mutableListOf<Line>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
            }
            MotionEvent.ACTION_MOVE -> {
                // Cập nhật toạ độ điểm cuối trong lúc kéo
                endX = x
                endY = y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                // Thêm đường thẳng hoàn chỉnh vào danh sách khi nhấc tay lên
                lines.add(Line(startX, startY, endX, endY))
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Vẽ tất cả các đường thẳng trong danh sách
        for (line in lines) {
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint)
        }

        // Vẽ đường thẳng hiện tại khi đang kéo tay
        canvas.drawLine(startX, startY, endX, endY, paint)
    }
}


