package com.canvas.paint.panda.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class CalligraphyView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val path = Path()
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        strokeWidth = 50f
    }

    private val splashPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private var lastX = 0f
    private var lastY = 0f

    // Đệm bitmap để vẽ các giọt mực
    private lateinit var bufferBitmap: Bitmap
    private lateinit var bufferCanvas: Canvas



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bufferBitmap = Bitmap.createBitmap(5000, 5000, Bitmap.Config.ARGB_8888)
        bufferCanvas = Canvas(bufferBitmap)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                createRaindropSplashes(x, y) // Tạo các giọt mực tung tóe
                invalidate()

                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_UP -> {
                // Xử lý khi ngón tay được nâng lên (có thể làm gì đó khi ngừng vẽ)
            }
        }
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Vẽ đường path chính lên canvas của view
        canvas.drawPath(path, paint)

        // Vẽ bitmap đệm chứa các giọt mực lên canvas của view
        canvas.drawBitmap(bufferBitmap, 0f, 0f, null)
    }

    private fun createRaindropSplashes(x: Float, y: Float) {
        // Số lượng giọt mưa rơi xuống
        val splashCount = Random.nextInt(5, 15)

        for (i in 0 until splashCount) {
            // Kích thước và khoảng cách ngẫu nhiên cho giọt mưa
            val width = Random.nextFloat() * 30 + 10  // Độ rộng của giọt mưa
            val height = Random.nextFloat() * 40 + 20 // Độ cao của giọt mưa

            val offsetX = Random.nextFloat() * 50 - 25 // Độ lệch xung quanh điểm vẽ
            val offsetY = Random.nextFloat() * 50 - 25 // Độ lệch yung quanh điểm vẽ

            // Vẽ giọt mưa vào bitmap đệm (hình bầu dục)
            bufferCanvas.drawOval(
                RectF(x + offsetX - width / 2, y + offsetY - height / 2, x + offsetX + width / 2, y + offsetY + height / 2),
                splashPaint
            )
        }
    }
}