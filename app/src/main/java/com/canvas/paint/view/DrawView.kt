package com.canvas.paint.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.canvas.paint.attribute.AttributeDrawView
import java.util.Stack

class DrawView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * Config các nét vẽ
     */
    val attributeDrawView: AttributeDrawView = AttributeDrawView()

    /**
     * Ngăn xếp lưu bitmap để hoàn tác các thay đổi
     */
    private val undoStack: Stack<Bitmap> = Stack()

    /**
     * Ngăn xếp lưu bitmap để làm lại các thay đổi
     */
    private val redoStack: Stack<Bitmap> = Stack()

    /**
     * Thiết lập layer dạng software để cải thiện hiệu suất vẽ
     * Thêm dữ liệu bitmap vào đối tượng attributeDraw
     */
    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setListDataBitmap()

    }

    fun setListDataBitmap() {
        attributeDrawView.addDataBitmap(context)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Vẽ Bitmap đã lưu lên canvas
        attributeDrawView.bitmapBuffer?.let { bitmapBuffer ->
            canvas.drawBitmap(bitmapBuffer, 0f, 0f, null)
        }

        if (attributeDrawView.isEraser) {
            attributeDrawView.eraser(canvas)
        } else {
            attributeDrawView.draw(canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        attributeDrawView.bitmapBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x // Lấy tọa độ x khi chạm
        val y = event.y // Lấy tọa độ y khi chạm
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                attributeDrawView.clearListPointBitmap() // Xóa điểm bitmap trước đó
                saveState() // Lưu trạng thái bitmap hiện tại trước khi bắt đầu vẽ mới
                if (!attributeDrawView.isEraser) {
                    attributeDrawView.addPointDownDraw(x, y) // Bắt đầu vẽ với điểm đầu tiên
                } else {
                    attributeDrawView.pathEraser.moveTo(
                        x,
                        y
                    ) // Di chuyển path eraser đến điểm chạm nếu đang ở chế độ tẩy
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (!attributeDrawView.isEraser) {
                    attributeDrawView.addPointMoveDraw(x, y) // Thêm điểm vào đường vẽ khi di chuyển
                } else {
                    attributeDrawView.pathEraser.lineTo(x, y) // Thêm điểm vào đường xóa khi di chuyển
                }
                invalidate() // Làm mới view để hiển thị các thay đổi
            }

            MotionEvent.ACTION_UP -> {
                drawNewBitmapBuffer()
                attributeDrawView.pathPaint = Path() // Đặt lại path vẽ khi ngón tay nhấc lên
                attributeDrawView.pathEraser = Path() // Đặt lại path tẩy
                attributeDrawView.clearListPointBitmap() // Xóa danh sách điểm bitmap sau khi vẽ xong
            }
        }

        return true // Trả về true để xác nhận xử lý sự kiện chạm
    }

    /**
     * Tạo một Bitmap mới từ Canvas của view
     * Để lưu cái bitmap mới nhất vào bitmapBuffer
     */
    private fun drawNewBitmapBuffer() {
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        attributeDrawView.bitmapBuffer = bitmap
    }

    /**
     * Lưu trạng thái các nét vẽ
     * Hàm này sẽ tạo một bản sao của bitmap hiện tại và thêm vào ngăn xếp undo,
     * đảm bảo trạng thái vẽ trước đó có thể khôi phục khi cần.
     * Mỗi lần lưu trạng thái mới, ngăn xếp redo sẽ bị xóa
     * để đảm bảo các thao tác redo trước đó không còn hiệu lực.
     */
    private fun saveState() {
        attributeDrawView.bitmapBuffer?.let { currentBitmap ->
            // Tạo bản sao của bitmap hiện tại và thêm vào ngăn xếp undo
            val bitmapCopy = currentBitmap.copy(currentBitmap.config, true)
            undoStack.push(bitmapCopy)
            // Xóa ngăn xếp redo khi lưu trạng thái mới
            redoStack.clear()
        }
    }

    /**
     * Quay lại nét vẽ trước đó
     * Hàm này sẽ khôi phục lại bitmap trước đó từ ngăn xếp undo
     * và lưu bitmap hiện tại vào ngăn xếp redo.
     * Nếu có bitmap trước đó trong ngăn xếp undo, nó sẽ khôi phục
     * lại trạng thái vẽ trước đó và vẽ lại view.
     */
    fun undo() {
        if (undoStack.isNotEmpty()) {
            // Lấy bitmap trên cùng từ ngăn xếp undo
            val bitmapToRestore = undoStack.pop()
            redoStack.push(attributeDrawView.bitmapBuffer) // Đẩy bitmap hiện tại vào ngăn xếp redo
            attributeDrawView.bitmapBuffer = bitmapToRestore // Khôi phục bitmap
            invalidate() // Vẽ lại view sau khi undo
        }
    }

    /**
     * Khôi phục lại nét vẽ đã hoàn tác
     * Hàm này sẽ khôi phục lại bitmap từ ngăn xếp redo
     * và lưu bitmap hiện tại vào ngăn xếp undo.
     * Nếu có bitmap trong ngăn xếp redo, nó sẽ khôi phục
     * lại trạng thái vẽ trước khi hoàn tác và vẽ lại view.
     */
    fun redo() {
        if (redoStack.isNotEmpty()) {
            val bitmapToRestore = redoStack.pop()
            undoStack.push(attributeDrawView.bitmapBuffer) // Đẩy bitmap hiện tại vào ngăn xếp undo
            attributeDrawView.bitmapBuffer = bitmapToRestore // Khôi phục bitmap
            invalidate() // Vẽ lại view sau khi redo
        }
    }

    fun clear() {
        attributeDrawView.bitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        invalidate()
    }
}
