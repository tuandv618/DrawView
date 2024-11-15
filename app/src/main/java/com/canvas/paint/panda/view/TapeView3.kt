package com.canvas.paint.panda.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.canvas.paint.R
import com.canvas.paint.panda.model.Line

class TapeView3(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        isDither = true
        isFilterBitmap = true
    }

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    // Danh sách lưu trữ các đường thẳng đã vẽ
    private val lines = mutableListOf<Line>()

    // Danh sách các icon với kích thước khác nhau
    private val listIconBitmap = listOf(
        BitmapFactory.decodeResource(resources, R.drawable.emoji14_1),
        BitmapFactory.decodeResource(resources, R.drawable.emoji14_2),
        BitmapFactory.decodeResource(resources, R.drawable.emoji14_3),
//        BitmapFactory.decodeResource(resources, R.drawable.emoji14_4),
//        BitmapFactory.decodeResource(resources, R.drawable.emoji01_5),
//        BitmapFactory.decodeResource(resources, R.drawable.emoji01_6),
    )

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
                endX = x
                endY = y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                lines.add(Line(startX, startY, endX, endY))
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (line in lines) {
            drawLineWithIcons(canvas, line)
        }
        drawLineWithIcons(canvas, Line(startX, startY, endX, endY))
    }

    private fun drawLineWithIcons(canvas: Canvas, line: Line) {
        val dx = line.endX - line.startX
        val dy = line.endY - line.startY
        val lineLength = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()

        // Góc của đường thẳng
        val angle = Math.atan2(dy.toDouble(), dx.toDouble()).toFloat()

        // Tọa độ ban đầu
        var x = line.startX
        var y = line.startY

        // Duyệt qua danh sách icon và vẽ chúng dọc theo đường thẳng
        var remainingLength = lineLength
        var iconIndex = 0

        while (remainingLength > 0) {
            val icon = listIconBitmap[iconIndex % listIconBitmap.size]
            val iconSpacing = icon.width.toFloat()

            // Vẽ icon tại vị trí hiện tại
            canvas.drawBitmap(icon, x, y, paint)

            // Di chuyển đến vị trí tiếp theo theo hướng của góc angle
            x += Math.cos(angle.toDouble()).toFloat() * iconSpacing
            y += Math.sin(angle.toDouble()).toFloat() * iconSpacing

            // Cập nhật chiều dài còn lại và chuyển đến icon tiếp theo
            remainingLength -= iconSpacing
            iconIndex++
        }
    }
}


