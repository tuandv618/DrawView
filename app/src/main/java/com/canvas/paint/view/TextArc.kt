package com.canvas.paint.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
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
    private var text = "Hello, this is a curved text!" // Văn bản sẽ hiển thị

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Đặt lại Path
        path.reset()

        // Tính chiều dài của văn bản
        val textLength = paint.measureText(text)

        // Xác định điểm bắt đầu và kết thúc dựa trên chiều dài văn bản
        val startX = (width / 2 - textLength / 2) // Bắt đầu ở giữa màn hình
        val endX = (width / 2 + textLength / 2)   // Kết thúc ở giữa màn hình

        // Vẽ đường cong từ startX đến endX
        path.moveTo(startX, 500f)
        path.quadTo(width / 2F, controlPointY, endX, 500f)

        // Vẽ đường cong trên Canvas
        canvas.drawPath(path, paint)

        // Vẽ văn bản theo đường cong
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

    fun setText(newText: String) {
        text = newText
        invalidate() // Yêu cầu vẽ lại khi có văn bản mới
    }
}
