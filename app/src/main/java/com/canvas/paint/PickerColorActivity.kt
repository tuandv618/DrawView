package com.canvas.paint

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.canvas.paint.color.ColorPickerView
import com.canvas.paint.color.listeners.ColorListener


class PickerColorActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_color)


        findViewById<ColorPickerView>(R.id.colorPickerView).setPaletteDrawable(ContextCompat.getDrawable(this, R.drawable.color_picker_view_bg))


        findViewById<ColorPickerView>(R.id.colorPickerView).setColorListener(ColorListener { color, fromUser ->
            findViewById<Button>(R.id.btnPickColor).setBackgroundColor(color)
        })

    }
}