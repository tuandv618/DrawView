package com.canvas.paint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.canvas.paint.view.TextPath

class MainActivity : AppCompatActivity() {
    private lateinit var textPath: TextPath
    private lateinit var seekBar: SeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textPath = findViewById(R.id.tvArc2)
        seekBar = findViewById(R.id.seekBar)

        // Thay đổi độ cong khi SeekBar được di chuyển
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textPath.setCurvature(progress.toFloat())
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