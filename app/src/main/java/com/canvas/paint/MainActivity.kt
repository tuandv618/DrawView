package com.canvas.paint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.canvas.paint.view.TextArc

class MainActivity : AppCompatActivity() {
    private lateinit var customView: TextArc
    private lateinit var seekBar: SeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customView = findViewById(R.id.tvArc)
        seekBar = findViewById(R.id.seekBar)

        // Đặt giá trị mặc định cho SeekBar
        seekBar.max = 180 // Giá trị tối đa

        customView.setText("Tuấn đẹp trai")

        // Thay đổi độ cong khi SeekBar được di chuyển
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                customView.updateControlPoint(progress.toFloat()) // Cập nhật độ cong
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Không cần thực hiện gì ở đây
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Không cần thực hiện gì ở đây
            }
        })
    }

}