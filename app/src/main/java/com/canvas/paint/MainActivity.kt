package com.canvas.paint

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.canvas.paint.brush.BrushTypeDraw
import com.canvas.paint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClear.setOnClickListener {
            binding.drawView.clear()
            binding.drawView.setListDataBitmap()
        }

        binding.btnDraw.setOnClickListener {
            binding.drawView.attributeDraw.setEraserEnable(false)
        }

        binding.btnEraser.setOnClickListener {
            binding.drawView.attributeDraw.setEraserEnable(true)
        }

        binding.btnBrushNone.setOnClickListener {
            binding.drawView.attributeDraw.brushTypeDraw = BrushTypeDraw.BRUSH_NONE
        }

        binding.btnBitmap.setOnClickListener {
            binding.drawView.attributeDraw.brushTypeDraw = BrushTypeDraw.BRUSH_BITMAP
        }

        binding.btUndo.setOnClickListener {
            binding.drawView.undo()
        }

        binding.btRedo.setOnClickListener {
            binding.drawView.redo()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (binding.drawView.attributeDraw.isEraser) {
                    binding.drawView.attributeDraw.setSizeStrokeEraser(progress.toFloat())
                } else {
                    binding.drawView.attributeDraw.setSizeStrokePaint(
                        this@MainActivity,
                        progress.toFloat()
                    )
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }
}