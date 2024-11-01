package com.canvas.paint.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.canvas.paint.attribute.AttributeDraw

class CustomView (context: Context, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * Config các nét vẽ
     */
    val attributeDraw: AttributeDraw = AttributeDraw()

    /**
     * Thiết lập layer dạng software để cải thiện hiệu suất vẽ
     * Thêm dữ liệu bitmap vào đối tượng attributeDraw
     */
    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setListDataBitmap()

    }

    fun setListDataBitmap(){
        attributeDraw.addDataBitmap(context)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Kiểm tra giá trị currentBitmap
        Log.d("DrawView", "currentBitmap: $currentBitmap")

        currentBitmap?.let {
            canvas.drawBitmap(it, 0F, 0F, null)
        }

        if (attributeDraw.isEraser) {
            attributeDraw.eraser(canvas)
        } else {
            attributeDraw.draw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x // Lấy tọa độ x khi chạm
        val y = event.y // Lấy tọa độ y khi chạm
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                attributeDraw.clearListPointBitmap() // Xóa điểm bitmap trước đó
                if (!attributeDraw.isEraser) {
                    attributeDraw.addPointDownDraw(x, y) // Bắt đầu vẽ với điểm đầu tiên
                } else {
                    attributeDraw.pathEraser.moveTo(x, y) // Di chuyển path eraser đến điểm chạm nếu đang ở chế độ tẩy
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (!attributeDraw.isEraser) {
                    attributeDraw.addPointMoveDraw(x, y) // Thêm điểm vào đường vẽ khi di chuyển
                } else {
                    attributeDraw.pathEraser.lineTo(x, y) // Thêm điểm vào đường xóa khi di chuyển
                }
                invalidate() // Làm mới view để hiển thị các thay đổi
            }

            MotionEvent.ACTION_UP -> {
                saveBitmap()
                attributeDraw.pathPaint = Path() // Đặt lại path vẽ khi ngón tay nhấc lên
                attributeDraw.pathEraser = Path() // Đặt lại path tẩy
                attributeDraw.clearListPointBitmap() // Xóa danh sách điểm bitmap sau khi vẽ xong
            }
        }

        return true // Trả về true để xác nhận xử lý sự kiện chạm
    }
    private var currentBitmap: Bitmap? = null

    private fun saveBitmap() {
        // Kiểm tra kích thước của view
        if (measuredWidth <= 0 || measuredHeight <= 0) {
            Log.e("DrawView", "Kích thước view không hợp lệ: ${measuredWidth}x${measuredHeight}")
            return
        }

        val newBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        this.draw(canvas)
        currentBitmap = newBitmap

        // Log để kiểm tra xem currentBitmap đã được lưu
        if (currentBitmap != null) {
            Log.d("DrawView", "currentBitmap đã được lưu thành công!")
        }
    }



}
