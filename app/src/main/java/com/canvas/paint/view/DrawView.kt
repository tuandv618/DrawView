package com.canvas.paint.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.canvas.paint.config.DrawConfig
import java.util.Stack

class DrawView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    val drawConfig: DrawConfig = DrawConfig()

    private val undoStack: Stack<Bitmap> = Stack()
    private val redoStack: Stack<Bitmap> = Stack()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Vẽ Bitmap đã lưu lên canvas
        drawConfig.bitmapBuffer?.let { bitmapBuffer ->
            canvas.drawBitmap(bitmapBuffer, 0f, 0f, null)
        }

        if (drawConfig.isEraser) {
            drawConfig.canvasBuffer?.drawPath(drawConfig.pathEraser, drawConfig.paintEraser)
        } else {
            drawConfig.canvasBuffer?.drawPath(drawConfig.pathPaint, drawConfig.paintPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawConfig.bitmapBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawConfig.bitmapBuffer?.let { bitmapBuffer ->
            drawConfig.canvasBuffer = Canvas(bitmapBuffer)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawConfig.clearListPointBitmap()
                saveState() // Lưu trạng thái trước khi bắt đầu vẽ mới
                if (!drawConfig.isEraser) {
                    drawConfig.addPointDownDraw(x, y)
                } else {
                    drawConfig.pathEraser.moveTo(x, y)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (!drawConfig.isEraser) {
                    drawConfig.addPointMoveDraw(x, y)
                } else {
                    drawConfig.pathEraser.lineTo(x, y)
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                drawConfig.pathPaint = Path()
                drawConfig.pathEraser = Path()
                drawConfig.clearListPointBitmap()
            }
        }

        return true
    }

    private fun saveState() {
        drawConfig.bitmapBuffer?.let { currentBitmap ->
            // Tạo bản sao của bitmap hiện tại và thêm vào ngăn xếp undo
            val bitmapCopy = currentBitmap.copy(currentBitmap.config, true)
            undoStack.push(bitmapCopy)
            // Xóa ngăn xếp redo khi lưu trạng thái mới
            redoStack.clear()
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            // Lấy bitmap trên cùng từ ngăn xếp undo
            val bitmapToRestore = undoStack.pop()
            redoStack.push(drawConfig.bitmapBuffer) // Đẩy bitmap hiện tại vào ngăn xếp redo
            drawConfig.bitmapBuffer = bitmapToRestore // Khôi phục bitmap
            drawConfig.bitmapBuffer?.let { bitmapBuffer ->
                drawConfig.canvasBuffer = Canvas(bitmapBuffer)
            }

            invalidate() // Vẽ lại view sau khi undo
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val bitmapToRestore = redoStack.pop()
            undoStack.push(drawConfig.bitmapBuffer) // Đẩy bitmap hiện tại vào ngăn xếp undo
            drawConfig.bitmapBuffer = bitmapToRestore // Khôi phục bitmap
            drawConfig.bitmapBuffer?.let { bitmapBuffer ->
                drawConfig.canvasBuffer = Canvas(bitmapBuffer)
            }
            invalidate() // Vẽ lại view sau khi redo
        }
    }
}
