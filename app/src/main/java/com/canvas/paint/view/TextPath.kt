package com.canvas.paint.view
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
class TextPath  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
        textSize = 60f  // Đặt kích thước văn bản
    }

    private val path = Path()

    // Biến để lưu trữ độ cong (0 là thẳng, giá trị lớn hơn 180 thì cong xuống, nhỏ hơn 180 thì cong ngược lên)
    private var curvature = 180f // Mặc định đặt curvature ở 180

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Lấy kích thước của View
        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        // Văn bản cần hiển thị
        val text = "Hello cute!"
        val textLength = paint.measureText(text)  // Độ dài của văn bản

        // Tạo hình chữ nhật với chiều dài tương ứng với độ dài văn bản
        val left = (viewWidth - textLength) / 2 // Căn giữa theo chiều ngang
        val right = left + textLength
        val top: Float
        val bottom: Float

        // Tính toán độ cong dựa trên giá trị của SeekBar
        when {
            curvature > 180 -> { // Nếu lớn hơn 180 thì giống cái đầu (vòng cung đi lên)
                top = viewHeight * 0.5f - (curvature - 180) // Tính toán chiều cao trên
                bottom = viewHeight * 0.5f + (curvature - 180) // Tính toán chiều cao dưới
                // Vẽ vòng cung bình thường
                path.reset()
                path.addArc(RectF(left, top, right, bottom), 180f, 180f)  // Đường cong xuống

            }
            curvature < 180 -> { // Nếu nhỏ hơn 180 thì giống vòng cung đi xuống
                val downCurve = 180 - curvature  // Điều chỉnh độ cong
                top = viewHeight * 0.5f - downCurve // Tính toán chiều cao trên
                bottom = viewHeight * 0.5f + downCurve // Tính toán chiều cao dưới
                path.reset()
                path.addArc(RectF(left, top, right, bottom), 180f, -180f)  // Đường cong xuống
            }
            else -> { // Nếu bằng 180 thì thẳng
                top = viewHeight * 0.5f
                bottom = viewHeight * 0.5f
                path.reset()  // Đường thẳng
                path.moveTo(left, top)
                path.lineTo(right, top)
            }
        }

        // Vẽ Path lên canvas
        canvas.drawPath(path, paint)

        // Tính toán độ dài của path
        val measure = PathMeasure(path, false)
        val pathLength = measure.length

        // Tính toán vị trí văn bản để căn giữa
        val startPos = (pathLength - textLength) / 2  // Bắt đầu vẽ văn bản từ vị trí giữa

        // Vẽ văn bản lên đường path
        measure.getPosTan(startPos, pos, null) // Lấy vị trí và góc của văn bản
        canvas.drawTextOnPath(text, path, startPos, 0f, paint)
    }



    // Hàm cập nhật độ cong
    fun setCurvature(value: Float) {
        curvature = value
        invalidate() // Vẽ lại View khi độ cong thay đổi
    }

    // Biến để lưu trữ vị trí
    private val pos = FloatArray(2)  // Mảng 2 chiều để lưu trữ vị trí (x, y)
}