package com.canvas.paint

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.canvas.paint.drawview.enums.DrawingMode
import com.canvas.paint.drawview.views.DrawView

class MainActivity : AppCompatActivity() {

   var mDrawView : DrawView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Xin chào

        findViewById<View>(R.id.btnRedo).setOnClickListener {
            mDrawView?.redo()
            //mDrawView?.drawingMode = DrawingMode.ERASER
        }

        findViewById<View>(R.id.btnUndo).setOnClickListener {
            mDrawView?.undo()
        }

        findViewById<View>(R.id.btnEraser).setOnClickListener {
            mDrawView?.drawingMode = DrawingMode.ERASER
        }

        findViewById<View>(R.id.btnRed).setOnClickListener {
            mDrawView?.drawingMode = DrawingMode.DRAW
            mDrawView?.drawColor = Color.parseColor("#FF5722")
        }

        findViewById<View>(R.id.btnBLue).setOnClickListener {
            mDrawView?.drawingMode = DrawingMode.DRAW
            mDrawView?.drawColor = Color.parseColor("#00BCD4")
        }

        findViewById<View>(R.id.btnBackground).setOnClickListener {
            mDrawView?.setDrawViewBackgroundColor(Color.parseColor("#FF5722"))
        }


        findViewById<SeekBar>(R.id.seekBar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mDrawView?.drawWidth = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        mDrawView = findViewById(R.id.draw_view)

        // Đây là commit của master
        // Đây là commit của test01
    }

}