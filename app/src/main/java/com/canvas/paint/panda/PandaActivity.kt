package com.canvas.paint.panda

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import com.canvas.paint.R
import com.canvas.paint.databinding.ActivityPandaBinding
import com.canvas.paint.panda.view.CustomView

class PandaActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPandaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEraser.setOnClickListener {
            //binding.customView.enableEraser()
        }

        binding.btnDraw.setOnClickListener {
            //binding.customView.disableEraser()
        }

        binding.sbSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.customView.iconScale = progress / 100f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

    }
}