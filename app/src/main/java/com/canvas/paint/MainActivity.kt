package com.canvas.paint

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.canvas.paint.brush.BrushTypeDrawView
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
            binding.drawView.attributeDrawView.setEraserEnable(false)
        }

        binding.btnEraser.setOnClickListener {
            binding.drawView.attributeDrawView.setEraserEnable(true)
        }

        binding.btnBrushNone.setOnClickListener {
            binding.drawView.attributeDrawView.brushTypeDrawView = BrushTypeDrawView.BRUSH_NONE
        }

        binding.btnBitmap.setOnClickListener {
            binding.drawView.attributeDrawView.brushTypeDrawView = BrushTypeDrawView.BRUSH_BITMAP
        }

        binding.btUndo.setOnClickListener {
            binding.drawView.undo()
        }

        binding.btRedo.setOnClickListener {
            binding.drawView.redo()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (binding.drawView.attributeDrawView.isEraser) {
                    binding.drawView.attributeDrawView.setSizeStrokeEraser(progress.toFloat())
                } else {
                    binding.drawView.attributeDrawView.setSizeStrokePaint(
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