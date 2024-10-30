package com.canvas.paint.panda.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.canvas.paint.R
import kotlin.math.sqrt

class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * Kích thước icon, nét vẽ
     */
    private val sizeBrush = 30f
    private val spaceBitmap = 30f

    private var path = Path()
    private var paint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        strokeWidth = sizeBrush
    }

    private val icon: Bitmap // Dữ liệu cần vẽ
    private val icon2: Bitmap // Dữ liệu cần vẽ
    private val icon3: Bitmap // Dữ liệu cần vẽ
    private val icon4: Bitmap // Dữ liệu cần vẽ
    private val icon5: Bitmap // Dữ liệu cần vẽ
    private val icon6: Bitmap // Dữ liệu cần vẽ
    private val points = mutableListOf<PointF>() // Danh sách để lưu trữ các điểm


    private var scaledBitmapStroke: Bitmap? = null
    // Tạo Bitmap và Canvas riêng để vẽ
    private lateinit var drawCanvas: Canvas
    private lateinit var canvasBitmap: Bitmap
    private var bitmapStroke: VectorDrawable? = null // Data của stroke


    private var eraserPaint = Paint()  // Paint của tẩy
    private var isEraserOn = false


    // Chức năng tẩy
    fun enableEraser() {
        isEraserOn = true
        eraserPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 50000F
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    // Chức năng vẽ bình thường
    fun disableEraser() {
        isEraserOn = false
        paint = Paint().apply {
            color = Color.TRANSPARENT
            style = Paint.Style.STROKE
            strokeWidth = sizeBrush
        }
    }



    // Tạo Bitmap và Canvas riêng để vẽ
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scaledBitmapStroke = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        scaledBitmapStroke?.let { scaledBitmapStroke ->
            val canvasStroke = Canvas(scaledBitmapStroke)
            bitmapStroke?.setBounds(0, 0, w, h)
            bitmapStroke?.draw(canvasStroke)
        }
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // Lấy bitmap từ drawable
        var drawable = ContextCompat.getDrawable(context, R.drawable.emoji14_1)
        icon = drawable?.toBitmap(sizeBrush.toInt(), sizeBrush.toInt()) ?: Bitmap.createBitmap(
            sizeBrush.toInt(),
            sizeBrush.toInt(),
            Bitmap.Config.ARGB_8888
        ) // Kích thước mặc định nếu không có drawable

        drawable = ContextCompat.getDrawable(context, R.drawable.emoji14_2)
        icon2 = drawable?.toBitmap(sizeBrush.toInt(), sizeBrush.toInt()) ?: Bitmap.createBitmap(
            sizeBrush.toInt(),
            sizeBrush.toInt(),
            Bitmap.Config.ARGB_8888
        ) // Kích thước mặc định nếu không có drawable

        drawable = ContextCompat.getDrawable(context, R.drawable.emoji14_3)
        icon3 = drawable?.toBitmap(sizeBrush.toInt(), sizeBrush.toInt()) ?: Bitmap.createBitmap(
            sizeBrush.toInt(),
            sizeBrush.toInt(),
            Bitmap.Config.ARGB_8888
        ) // Kích thước mặc định nếu không có drawable

        drawable = ContextCompat.getDrawable(context, R.drawable.emoji14_1)
        icon4 = drawable?.toBitmap(sizeBrush.toInt(), sizeBrush.toInt()) ?: Bitmap.createBitmap(
            sizeBrush.toInt(),
            sizeBrush.toInt(),
            Bitmap.Config.ARGB_8888
        ) // Kích thước mặc định nếu không có drawable

        drawable = ContextCompat.getDrawable(context, R.drawable.emoji14_2)
        icon5 = drawable?.toBitmap(sizeBrush.toInt(), sizeBrush.toInt()) ?: Bitmap.createBitmap(
            sizeBrush.toInt(),
            sizeBrush.toInt(),
            Bitmap.Config.ARGB_8888
        ) // Kích thước mặc định nếu không có drawable

        drawable = ContextCompat.getDrawable(context, R.drawable.emoji14_3)
        icon6 = drawable?.toBitmap(sizeBrush.toInt(), sizeBrush.toInt()) ?: Bitmap.createBitmap(
            sizeBrush.toInt(),
            sizeBrush.toInt(),
            Bitmap.Config.ARGB_8888
        ) // Kích thước mặc định nếu không có drawable
    }

    /**
     * Khi người dùng chạm xuống, nó bắt đầu một đường vẽ tại vị trí chạm và khi người dùng di chuyển ngón tay,
     * nó sẽ vẽ tiếp theo đường đã vẽ trước đó, đồng thời thêm các điểm vào danh sách nếu người dùng di chuyển đủ xa
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y
        Log.e("kh45", "x = $x, y = $y")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                points.clear()
                if(!isEraserOn){
                    points.add(PointF(x, y))  // Thêm điểm bắt đầu
                }else{
                    path.moveTo(x, y)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if(!isEraserOn){
                    // Thêm điểm vào danh sách với khoảng cách
                    if (points.size == 0 || points.last().distance(x, y) > (sizeBrush + spaceBitmap)) { // Thay đổi khoảng cách nếu cần
                        points.add(PointF(x, y))
                    }
                }else{
                    path.lineTo(x, y)
                }

                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                path = Path()
                points.clear()
            }
        }

        return true
    }

    /**
     * Hàm distance này để trong trường hợp cần kiểm tra khoảng cách giữa các điểm,
     * Kiểm tra xem người dùng đã người dùng đã di chuyển một khoảng cách đủ lớn để vẽ một điểm mới hay chưa.
     */
    private fun PointF.distance(x: Float, y: Float): Float {
        return sqrt(((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y)).toDouble()).toFloat()
    }

    /**
     * Vẽ lại view mới
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //canvas.drawPath(path, paint)
        drawBitmap(canvas)

        canvas.restore()
    }

    /**
     * Vẽ các nét vẽ bằng các icon dựa trên các tọa độ đã lưu
     */
    private fun drawBitmap(canvas: Canvas) {
        // Vẽ Bitmap đã lưu lên canvas
        canvas.drawBitmap(canvasBitmap, 0f, 0f, null)

        val paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true// Kích hoạt bộ lọc bitmap để làm mượt hơn
        }

        // Vẽ nét đang vẽ
        if (isEraserOn) {
            drawCanvas.drawPath(path, eraserPaint)  // Sử dụng cọ tẩy
        }else{
            // Vẽ icon tại các điểm
            var i = 0
            for (point in points) {
                var data = icon
                when(i){
                    1 -> {
                        data = icon2
                    }
                    2 -> {
                        data = icon3
                    }
                    3 -> {
                        data = icon4
                    }
                    4 -> {
                        data = icon5
                    }
                    5 -> {
                        data = icon6
                    }

                }


                scaleData(data)
                drawCanvas.drawBitmap(
                    data,
                    point.x - sizeBrush / 2,
                    point.y - sizeBrush / 2,
                    Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        isAntiAlias = true
                        isFilterBitmap = true
                        isDither = true
                    }
                ) // Vẽ icon tại vị trí

                if(i == 5){
                    i = 0
                }else{
                    i++
                }
            }
        }

    }

    fun scaleData(data : Bitmap){
        val scale = 2 // Tăng gấp đôi kích thước
        val bitmap = Bitmap.createBitmap(width * scale, height * scale, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
        canvas.setDrawFilter(PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG))
        canvas.drawBitmap(data, x * scale.toFloat(), y * scale.toFloat(), paint)
    }

}


