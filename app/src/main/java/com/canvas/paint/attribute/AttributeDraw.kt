package com.canvas.paint.attribute

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.canvas.paint.R
import com.canvas.paint.brush.BrushTypeDraw
import kotlin.math.sqrt

class AttributeDraw(
    var pathPaint: Path = Path(),
    var pathEraser: Path = Path(),
    var paintPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
    var paintEraser: Paint = Paint(Paint.ANTI_ALIAS_FLAG),
    private var sizePaint: Float = 30F,
    private var sizeEraser: Float = 30F,
    private var spacePoint: Float = 30F,
    private var colorPaint: Int = Color.BLACK,
    var isEraser: Boolean = false,
    var bitmapBuffer: Bitmap? = null,
    var canvasBuffer: Canvas? = null,
    private var countBitmap: Int = 0,
    private var listPointBitmap: MutableList<PointDraw> = mutableListOf(),
    private var listDataBitmap: MutableList<Bitmap> = mutableListOf(),
    var brushTypeDraw: BrushTypeDraw = BrushTypeDraw.BRUSH_BITMAP
) {

    init {
        initConfig()
    }

    private fun initConfig() {
        paintPaint.apply {
            color = colorPaint
            style = Paint.Style.STROKE
            strokeWidth = sizePaint
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

        paintEraser = Paint().apply {
            isAntiAlias = true
            strokeWidth = sizeEraser
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    /**
     * Tẩy  hoạc vẽ
     */
    fun setEraserEnable(isEraser: Boolean) {
        this.isEraser = isEraser
    }

    /**
     * Thay đổi kích thước nét tẩy
     */
    fun setSizeStrokeEraser(size: Float) {
        this.sizeEraser = size
        paintEraser.strokeWidth = size
    }

    /**
     * Thay đổi kích thước nét vẽ, và thay đổi kích thước bitmap để vẽ lên canvas luôn
     */
    fun setSizeStrokePaint(context: Context, size: Float) {
        this.sizePaint = size
        this.spacePoint = size
        paintPaint.strokeWidth = size
        if (brushTypeDraw == BrushTypeDraw.BRUSH_BITMAP) {
            addDataBitmap(context)
        }
    }


    /**
     * Thêm các nét vẽ khi người dùng vẽ, tùy thuộ vào brush type
     */
    fun addPointDownDraw(x: Float, y: Float) {
        when (brushTypeDraw) {
            BrushTypeDraw.BRUSH_NONE -> {
                addPointDown(x, y)
            }

            BrushTypeDraw.BRUSH_BITMAP -> {
                addPointBitmap(x, y)
            }
        }
    }

    /**
     * Thêm các nét vẽ bằng bitmap
     */
    fun addPointMoveDraw(x: Float, y: Float) {
        when (brushTypeDraw) {
            BrushTypeDraw.BRUSH_NONE -> {
                addPointMove(x, y)
            }

            BrushTypeDraw.BRUSH_BITMAP -> {
                addPointBitmap(x, y)
            }
        }
    }

    /**
     * Thêm các nét vẽ bình thường khi người dùng đè tay xuống
     */
    private fun addPointDown(x: Float, y: Float) {
        pathPaint.moveTo(x, y)
    }

    /**
     * Thêm các nét vẽ bình thường khi người dùng di chuyển tay
     */
    private fun addPointMove(x: Float, y: Float) {
        pathPaint.lineTo(x, y)
    }

    /**
     * Thêm các nét vẽ bình thường khi người dùng để tay lên
     */
    private fun addPointUp(x: Float, y: Float) {
        pathPaint.moveTo(x, y)
    }

    /**
     * Xóa toàn bộ vị trí vẽ để vẽ nét mới, cho count bitmap về đầu
     */
    fun clearListPointBitmap() {
        listPointBitmap.clear()
        countBitmap = 0
    }

    /**
     * Thêm điểm và bitmap mà người dùng muốn vẽ
     */
    private fun addPointBitmap(x: Float, y: Float) {
        if (listPointBitmap.size == 0 || listPointBitmap.last().isPointDistance(x, y)) {
            val pointDraw = PointDraw(PointF(x, y), countBitmap)
            listPointBitmap.add(pointDraw)

            countBitmap++
            if (countBitmap == listDataBitmap.size) {
                countBitmap = 0
            }
        }
    }

    /**
     * Tính toán khoảng cách để vẽ ảnh lên canvas
     */
    private fun PointDraw.isPointDistance(x: Float, y: Float): Boolean {
        return sqrt(((this.pointF.x - x) * (this.pointF.x - x) + (this.pointF.y - y) * (this.pointF.y - y)).toDouble()).toFloat() > (sizePaint + spacePoint)
    }

    /**
     * khởi tạo data cho vào list bitmap
     */
    fun addDataBitmap(context: Context) {
        listDataBitmap.clear()
        // Data 01
//        listDataBitmap.add(getBitmapFromDrawable(context, R.drawable.emoji01_1))
//        listDataBitmap.add(getBitmapFromDrawable(context, R.drawable.emoji01_2))
//        listDataBitmap.add(getBitmapFromDrawable(context, R.drawable.emoji01_3))
//        listDataBitmap.add(getBitmapFromDrawable(context, R.drawable.emoji01_4))
//        listDataBitmap.add(getBitmapFromDrawable(context, R.drawable.emoji01_5))
        // Data 04
        listDataBitmap.add(getBitmapFromDrawable(context, R.drawable.emoji04_1))
        listDataBitmap.add(getBitmapFromDrawable(context, R.drawable.emoji04_2))
        listDataBitmap.add(getBitmapFromDrawable(context, R.drawable.emoji04_3))
    }

    /**
     * Lấy bitmap từ drawable
     */
    private fun getBitmapFromDrawable(context: Context, data: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, data)
        return drawable?.toBitmap(sizePaint.toInt(), sizePaint.toInt()) ?: Bitmap.createBitmap(
            sizePaint.toInt(),
            sizePaint.toInt(),
            Bitmap.Config.ARGB_8888
        )
    }

    /**
     * Vẽ các nét vẽ lên canvas
     */
    fun draw(canvas: Canvas) {
        when (brushTypeDraw) {
            BrushTypeDraw.BRUSH_NONE -> {
                canvasBuffer?.drawPath(pathPaint, paintPaint)
            }

            BrushTypeDraw.BRUSH_BITMAP -> {
                drawPointBitmap(canvas)
            }
        }
    }

    /**
     * Vẽ các bitmap theo vị trí mà người dùng đã vẽ lên
     */
    private fun drawPointBitmap(canvas: Canvas) {
        listPointBitmap.forEachIndexed { index, pointDraw ->
            val bitmap = listDataBitmap[pointDraw.positionBitmap]
            val point = pointDraw.pointF
            canvasBuffer?.drawBitmap(
                bitmap,
                point.x - sizePaint / 2,
                point.y - sizePaint / 2,
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    isAntiAlias = true
                    isFilterBitmap = true
                    isDither = true
                }
            )
        }
    }

    /**
     * Vẽ tẩy lên bitmap lên canvas
     */
    fun eraser(canvas: Canvas) {
        canvasBuffer?.drawPath(
            pathEraser,
            paintEraser
        )
    }
}