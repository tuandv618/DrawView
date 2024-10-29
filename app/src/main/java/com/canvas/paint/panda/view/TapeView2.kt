package com.canvas.paint.panda.view



import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.canvas.paint.R
import com.canvas.paint.panda.model.Line

class TapeView2(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true // Cho phép khử răng cưa cho màu sắc
        isFilterBitmap = true // Khử răng cưa cho bitmap
    }

    private var sizeHeight = 80F

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f

    // Danh sách lưu trữ các đường thẳng đã vẽ
    private val lines = mutableListOf<Line>()

    // Hình ảnh để vẽ
    private var bitmap: Bitmap? = null
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    init {
        // Tải hình ảnh từ tài nguyên
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.tape67_1) // Thay 'your_image' bằng tên tài nguyên hình ảnh của bạn
        bitmapWidth = bitmap?.width ?: 0
        bitmapHeight = bitmap?.height ?: 0
    }

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
            // Vẽ hình ảnh trên đường thẳng
            drawLineWithBitmap(canvas, line.startX, line.startY, line.endX, line.endY, sizeHeight)
        }

        // Vẽ đường thẳng hiện tại khi đang kéo tay
        drawLineWithBitmap(canvas, startX, startY, endX, endY, sizeHeight)
    }

//    private fun drawLineWithBitmap(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float, desiredHeight: Float) {
//        // Tính toán độ dài và góc của đường thẳng
//        val dx = endX - startX
//        val dy = endY - startY
//        val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
//        val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()
//
//        if (bitmap != null && distance > 0) {
//            val originalBitmapWidth = bitmap!!.width
//            val originalBitmapHeight = bitmap!!.height
//
//
//            // Tỷ lệ giữa chiều cao mong muốn và chiều cao gốc của bitmap
//            val scale = desiredHeight / originalBitmapHeight
//            val scaledBitmapWidth = (originalBitmapWidth * scale).toInt().coerceAtLeast(1) // Đảm bảo scaledBitmapWidth > 0
//            val scaledBitmapHeight = desiredHeight.toInt().coerceAtLeast(1) // Đảm bảo scaledBitmapHeight > 0
//
//            // Tạo bitmap với kích thước mong muốn
//            val scaledBitmap = Bitmap.createScaledBitmap(bitmap!!, scaledBitmapWidth, scaledBitmapHeight, true)
//
//            var offsetX = 0f
//
//            // Lặp lại bitmap cho đến khi chiều dài đường thẳng được lấp đầy
//            while (offsetX < distance) {
//                // Tính toán chiều rộng của phần bitmap cần vẽ
//                val bitmapPartWidth = Math.min(scaledBitmapWidth.toFloat(), distance - offsetX).toInt().coerceAtLeast(1) // Đảm bảo bitmapPartWidth > 0
//
//                // Kiểm tra nếu `bitmapPartWidth` lớn hơn 0
//                if (bitmapPartWidth > 0) {
//                    // Tạo bitmap phần cần vẽ từ bitmap đã scale
//                    val bitmapPart = Bitmap.createBitmap(scaledBitmap, 0, 0, bitmapPartWidth, scaledBitmapHeight)
//
//                    canvas.save()
//                    canvas.rotate(angle, startX, startY)
//                    canvas.drawBitmap(bitmapPart, startX + offsetX, startY - scaledBitmapHeight / 2, paint)
//                    canvas.restore()
//                }
//
//                offsetX += bitmapPartWidth // Cập nhật offsetX cho vòng lặp
//            }
//        }
//    }

    private fun drawLineWithBitmap(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float, desiredHeight: Float) {
        val dx = endX - startX
        val dy = endY - startY
        val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()

        if (bitmap != null && distance > 0) {
            val originalBitmapWidth = bitmap!!.width
            val originalBitmapHeight = bitmap!!.height

            // Tỷ lệ scale để giữ nguyên tỷ lệ ảnh gốc và khử méo ảnh
            val scale = desiredHeight / originalBitmapHeight
            val scaledBitmapWidth = (originalBitmapWidth * scale).toInt().coerceAtLeast(1)
            val scaledBitmapHeight = desiredHeight.toInt().coerceAtLeast(1)

            // Scale bitmap và đảm bảo chất lượng bằng cách sử dụng Config.ARGB_8888
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap!!, scaledBitmapWidth, scaledBitmapHeight, true
            ).copy(Bitmap.Config.ARGB_8888, true)

            var offsetX = 0f

            // Vẽ từng phần của bitmap dọc theo chiều dài của line
            while (offsetX < distance) {
                val bitmapPartWidth = Math.min(scaledBitmapWidth.toFloat(), distance - offsetX).toInt().coerceAtLeast(1)

                // Kiểm tra nếu `bitmapPartWidth` hợp lệ
                if (bitmapPartWidth > 0) {
                    val bitmapPart = Bitmap.createBitmap(scaledBitmap, 0, 0, bitmapPartWidth, scaledBitmapHeight)

                    canvas.save()
                    canvas.rotate(angle, startX, startY)
                    canvas.drawBitmap(bitmapPart, startX + offsetX, startY - scaledBitmapHeight / 2, paint)
                    canvas.restore()
                }
                offsetX += bitmapPartWidth
            }
        }
    }


}
