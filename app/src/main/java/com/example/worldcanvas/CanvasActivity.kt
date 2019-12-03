package com.example.worldcanvas

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_canvas.*


class CanvasActivity : AppCompatActivity() {

    private var element: Int = 0
    private var colorPicker: PickColorDialog? = null
    private var position: Int = 0
    private val usedColors = mutableListOf<Int>()

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        element = intent.getIntExtra("Canvas", 0)
        position = intent.getIntExtra("Position", 0)
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putInt("Canvas", element)
        editor.apply()

        colorPicker = PickColorDialog(this)
        setContentView(R.layout.activity_canvas)
        usedColors.add(canvas_view.INITIAL_COLOR)
    }


    fun goToAR(view: View) {
        val intent = Intent(this, ArActivity::class.java)
        intent.putIntegerArrayListExtra("COLORS", ArrayList(usedColors))
        intent.putExtra("Object", element)
        intent.putExtra("Position", position)
        startActivity(intent)
    }

    fun pickColor(view: View) {
        colorPicker?.show()
    }


    fun changeColor(view: View) {
        colorPicker?.hide()
        val color = (view.background as ColorDrawable).color
        canvas_view.brush.color = color
        if (!usedColors.contains(color)) {
            usedColors.add(color)
        }
    }

}
