package com.canvas.paint.config

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import com.canvas.paint.view.BrushType
import kotlin.math.sqrt

class DrawConfig(
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
    private var listPointBitmap: MutableList<PointF> = mutableListOf(),
    private var listDataBitmap: MutableList<Bitmap> = mutableListOf(),
    var brushType: BrushType = BrushType.BRUSH_NONE
) {

    init {
        initConfig()
    }

    private fun initConfig() {
        paintPaint.apply {
            color = colorPaint
            style = Paint.Style.STROKE
            strokeWidth = sizePaint
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

    fun setEraserEnable(isEraser: Boolean) {
        this.isEraser = isEraser
    }

    fun setSizeStrokeEraser(size: Float) {
        this.sizeEraser = size
    }

    fun setSizeStrokePaint(size: Float) {
        this.sizePaint = size
    }

    fun addPointDownDraw(x: Float, y: Float) {
        when (brushType) {
            BrushType.BRUSH_NONE -> {
                addPointDown(x, y)
            }

            BrushType.BITMAP -> {
               addPointBitmap(x, y)
            }
        }
    }

    fun addPointMoveDraw(x: Float, y: Float) {
        when (brushType) {
            BrushType.BRUSH_NONE -> {
                addPointMove(x, y)
            }

            BrushType.BITMAP -> {
                addPointBitmap(x, y)
            }
        }
    }

    private fun addPointDown(x: Float, y: Float){
        pathPaint.moveTo(x, y)
    }

    private fun addPointMove(x: Float, y: Float){
        pathPaint.lineTo(x, y)
    }

    private fun addPointUp(x: Float, y: Float){
        pathPaint.moveTo(x, y)
    }

    fun clearListPointBitmap() {
        listPointBitmap.clear()
    }

    private fun addPointBitmap(x: Float, y: Float) {
        if (listPointBitmap.size == 0 || listPointBitmap.last().isPointDistance(x, y)) {
            listPointBitmap.add(PointF(x, y))
        }
    }

    private fun PointF.isPointDistance(x: Float, y: Float): Boolean {
        return sqrt(((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y)).toDouble()).toFloat() > (sizePaint + spacePoint)
    }
}