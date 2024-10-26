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
    }

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
            drawLineWithBitmap(canvas, line.startX, line.startY, line.endX, line.endY)
        }

        // Vẽ đường thẳng hiện tại khi đang kéo tay
        drawLineWithBitmap(canvas, startX, startY, endX, endY)
    }

//    private fun drawLineWithBitmap(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float) {
//        // Tính toán độ dài và góc của đường thẳng
//        val dx = endX - startX
//        val dy = endY - startY
//        val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
//        val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()
//
//        // Vẽ hình ảnh dọc theo đường thẳng
//        if (bitmap != null && distance > 0) {
//            // Tạo bitmap mới với chiều dài bằng chiều dài của đoạn đường thẳng
//            val scaledBitmap = Bitmap.createScaledBitmap(bitmap!!, distance.toInt(), bitmapHeight, true)
//
//            // Vẽ bitmap vào canvas theo vị trí của đường thẳng
//            canvas.save()
//            canvas.rotate(angle, startX, startY)
//
//            // Vẽ bitmap bắt đầu từ startX, startY
//            canvas.drawBitmap(scaledBitmap, startX, startY - bitmapHeight / 2, paint) // Căn giữa chiều cao của hình ảnh
//            canvas.restore()
//        }
//    }

    private fun drawLineWithBitmap(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float) {
        // Tính toán độ dài và góc của đường thẳng
        val dx = endX - startX
        val dy = endY - startY
        val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
        val angle = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()

        // Vẽ hình ảnh dọc theo đường thẳng
        if (bitmap != null && distance > 0) {
            val originalBitmapWidth = bitmap!!.width  // Kích thước gốc của bitmap
            val originalBitmapHeight = bitmap!!.height

            // Chiều dài của đường thẳng
            val lineWidth = Math.min(distance, originalBitmapWidth.toFloat())  // Chiều dài tối đa của line tương ứng với chiều dài đường thẳng
            var offsetX = 0f

            // Lặp lại bitmap cho đến khi chiều dài đường thẳng được lấp đầy
            while (offsetX < distance) {
                // Tính toán chiều rộng bitmap cần vẽ
                val bitmapPartWidth = Math.min(originalBitmapWidth.toFloat(), distance - offsetX)

                // Kiểm tra chiều rộng của phần bitmap
                if (bitmapPartWidth > 0) {
                    // Tạo bitmap con từ hình ảnh
                    val bitmapPart = Bitmap.createBitmap(bitmap!!, 0, 0, bitmapPartWidth.toInt(), originalBitmapHeight)

                    canvas.save()
                    canvas.rotate(angle, startX, startY)
                    canvas.drawBitmap(bitmapPart, startX + offsetX, startY - originalBitmapHeight / 2, paint)
                    canvas.restore()
                }

                offsetX += bitmapPartWidth // Cập nhật offsetX cho vòng lặp
            }
        }
    }








}
