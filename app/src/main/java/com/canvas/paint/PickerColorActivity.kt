package com.canvas.paint

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.canvas.paint.color.ColorPickerView
import com.canvas.paint.color.listeners.ColorListener
import com.canvas.paint.color.sliders.BrightnessSlideBar

// Tuấn đẹp trai

class PickerColorActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker_color)


        //findViewById<ColorPickerView>(R.id.colorPickerView).setPaletteDrawable(ContextCompat.getDrawable(this, R.drawable.color_picker_view_bg))


        findViewById<ColorPickerView>(R.id.colorPickerView).setColorListener(ColorListener { color, fromUser ->
            findViewById<Button>(R.id.btnPickColor).setBackgroundColor(color)
        })

        //this.colorPickerView.attachAlphaSlider(dialogBinding.alphaSlideBar);
        this.findViewById<ColorPickerView>(R.id.colorPickerView).attachBrightnessSlider(findViewById<BrightnessSlideBar>(R.id.brightnessSlideBar))

    }
}