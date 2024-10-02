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
        textSize = 60f // Kích thước chữ
        textAlign = Paint.Align.CENTER
    }

    private val path = Path()
    private var controlPointY = 200f // Giá trị Y mặc định cho điểm điều khiển
    private var text = "Hello, this is a curved text!" // Văn bản sẽ hiển thị

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Đặt lại Path
        path.reset()

        // Chia văn bản thành các dòng
        val lines = text.split("\n")
        val lineHeight = 50f // Chiều cao mỗi dòng, bạn có thể điều chỉnh giá trị này

        // Vẽ từng dòng theo đường cong
        for ((index, line) in lines.withIndex()) {
            // Tính chiều dài của từng dòng
            val textLength = paint.measureText(line)

            // Xác định điểm bắt đầu và kết thúc dựa trên chiều dài văn bản
            val startX = (width / 2 - textLength / 2)
            val endX = (width / 2 + textLength / 2)

            // Đặt đường cong cho từng dòng
            path.reset()
            path.moveTo(startX, height / 2f + (index * lineHeight)*2) // Điều chỉnh vị trí Y cho từng dòng
            path.quadTo(width / 2F, controlPointY + (index * lineHeight)*2, endX, height / 2f + (index * lineHeight)*2)

            // Vẽ đường cong trên Canvas

            paint.color = Color.RED
            canvas.drawPath(path, paint)

            // Vẽ văn bản theo đường cong
            paint.color = Color.BLUE
            canvas.drawTextOnPath(line, path, 0f, 0f, paint)
        }
    }





    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = (paint.measureText(text) + paddingLeft + paddingRight).toInt() // Chiều rộng dựa trên chiều dài văn bản
        val desiredHeight = (controlPointY + paddingTop + paddingBottom).toInt() // Chiều cao tối đa của đường cong

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

        setMeasuredDimension(width, height*2)
    }


    fun updateControlPoint(progress: Float) {
        val lines = text.split("\n")
        val maxTextLength = lines.maxOf { paint.measureText(it) } // Tính độ dài dòng dài nhất

        // Tạo hệ số tỷ lệ dựa trên chiều dài văn bản so với chiều rộng view
        val textLengthRatio = maxTextLength / width

        // Điều chỉnh controlPointY theo hệ số này để đảm bảo độ cong đồng nhất
        controlPointY = when {
            progress <= 50 -> {
                200f + (50f - progress) * 6 * textLengthRatio // Điều chỉnh dựa trên tỷ lệ
            }
            else -> {
                200f - (progress - 50) * 6 * textLengthRatio
            }
        }
        invalidate() // Yêu cầu vẽ lại
    }


    fun setText(newText: String) {
        text = newText
        invalidate() // Yêu cầu vẽ lại khi có văn bản mới
    }
}
