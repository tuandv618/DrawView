package com.canvas.paint.view
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
class TextPath @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
        textSize = 60f  // Đặt kích thước văn bản (bạn có thể thay đổi giá trị này)
    }

    private val path = Path()

    // Biến để lưu trữ độ cong (0 là thẳng, giá trị cao hơn là cong nhiều hơn)
    private var curvature = 0f

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
        val top = viewHeight * 0.5f - curvature // Điều chỉnh chiều cao trên theo độ cong
        val bottom = viewHeight * 0.5f + curvature // Điều chỉnh chiều cao dưới theo độ cong

        val rectF = RectF(left, top, right, bottom)

        // Vẽ vòng cung đối xứng từ -180 đến 180 độ (nửa trên của hình elip)
        path.reset()
        path.addArc(rectF, 180f, 180f)  // Đường cong sẽ được tạo ra với chiều dài bằng chiều dài văn bản

        // Vẽ Path lên canvas
        canvas.drawPath(path, paint)

        // Tính toán độ dài của path (nó sẽ bằng chiều dài văn bản)
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