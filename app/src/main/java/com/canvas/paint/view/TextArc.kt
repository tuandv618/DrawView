package com.canvas.paint.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.canvas.paint.R

/**
 * Text arc in Kotlin.
 */
class TextArc @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr
) {
    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
        textSize = 40f // Kích thước chữ
        textAlign = Paint.Align.CENTER
    }

    private val path = Path()
    private var controlPointY = 500f // Giá trị Y mặc định cho điểm điều khiển

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Đặt lại Path
        path.reset()

        // Vẽ đường cong từ (100, 500) đến (600, 500)
        path.moveTo(100f, 500f)
        path.quadTo(350f, controlPointY, 600f, 500f)

        // Vẽ đường cong trên Canvas
        canvas.drawPath(path, paint)

        // Vẽ văn bản theo đường cong
        val text = "Hello, this is a curved text!"
        paint.color = Color.BLUE // Màu văn bản
        canvas.drawTextOnPath(text, path, 0f, 0f, paint) // Vẽ văn bản theo đường cong
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

    fun updateControlPoint(progress: Float) {
        // Điều chỉnh độ cong theo progress
        controlPointY = when {
            progress <= 90 -> {
                // Kéo xuống từ 500f cho đến 0f tại 90 độ
                500f + (90f - progress) * 4 // Tăng độ di chuyển
            }
            else -> {
                // Kéo lên từ 500f cho đến 0f tại 180 độ
                500f - (progress - 90) * 4 // Tăng độ di chuyển
            }
        }
        invalidate() // Yêu cầu vẽ lại
    }
}