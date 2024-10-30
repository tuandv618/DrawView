package com.canvas.paint

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.canvas.paint.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDraw.setOnClickListener {
            binding.drawView.drawConfig.setEraserEnable(false)
        }

        binding.btnEraser.setOnClickListener {
            binding.drawView.drawConfig.setEraserEnable(true)
        }

        binding.btnBrushNone.setOnClickListener {

        }

        binding.btnBitmap.setOnClickListener {

        }

        binding.btUndo.setOnClickListener {
            binding.drawView.undo()
        }

        binding.btRedo.setOnClickListener {
            binding.drawView.redo()
        }
    }
}