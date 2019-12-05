package com.example.worldcanvas

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_canvas.*
import java.io.ByteArrayOutputStream


class CanvasActivity : AppCompatActivity() {

    var element: Int = 0
    var model: Int = 0
    var colorPicker: PickColorDialog? = null
    private var position: Int = 0
    private val usedColors = mutableListOf<Int>()

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        element = intent.getIntExtra("Canvas", 0)
        model = intent.getIntExtra("Model", 0)
        position = intent.getIntExtra("Position", 0)
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putInt("Canvas", element)
        editor.putInt("Model", model)
        editor.apply()

        colorPicker = PickColorDialog(this)
        setContentView(R.layout.activity_canvas)
        usedColors.add(canvas_view.INITIAL_COLOR)
    }

    override fun onBackPressed() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }

    fun goToAR(view: View) {
        val intent = Intent(this, ArActivity::class.java)
        intent.putExtra("Object", element)
        intent.putExtra("Position", position)
        startActivity(intent)
    }


    fun goToARWithColors(view: View) {
        val intent = Intent(this, ArActivity::class.java)
        intent.putIntegerArrayListExtra("COLORS", ArrayList(usedColors))
        intent.putExtra("Object", element)
        intent.putExtra("Position", position)
        startActivity(intent)
    }

    fun goToARWithBitmap(view: View) {
        val intent = Intent(this, ArActivity::class.java)
        intent.putExtra("Object", element)
        intent.putExtra("Position", position)
        val stream = ByteArrayOutputStream()
        canvas_view.bmpImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        intent.putExtra("ANIMAL_IMAGE", byteArray)
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
