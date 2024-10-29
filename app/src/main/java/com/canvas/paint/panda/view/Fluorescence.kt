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

class Fluorescence(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val path = Path() // Đường vẽ hiện tại
    private val outerPaint = Paint().apply {
        color = Color.BLUE // Màu viền ngoài
        style = Paint.Style.STROKE
        strokeWidth = 60f // Độ dày viền ngoài
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        setShadowLayer(60f, 0f, 0f, Color.parseColor("#AA0000FF")) // Bóng xanh nhạt và lan rộng hơn
    }


    private val innerPaint = Paint().apply {
        color = Color.WHITE // Màu bên trong
        style = Paint.Style.STROKE
        strokeWidth = 58f // Độ dày nét bên trong
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND // Làm tròn đầu của nét vẽ ngoài
    }

    init {
        // Cần bật lớp phần mềm để hỗ trợ hiệu ứng bóng
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Bắt đầu vẽ đường từ điểm bắt đầu
                path.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                // Thêm đường vẽ đến điểm hiện tại
                path.lineTo(x, y)
                invalidate() // Yêu cầu vẽ lại
            }
            MotionEvent.ACTION_UP -> {
                // Dừng vẽ khi nhấc ngón tay lên
                path.lineTo(x, y)
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Vẽ đường viền ngoài (border)
        canvas.drawPath(path, outerPaint)
        // Vẽ nét bên trong
        canvas.drawPath(path, innerPaint)
    }

}

// Data class for storing line coordinates
data class Line(val startX: Float, val startY: Float, val endX: Float, val endY: Float)
