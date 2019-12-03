package com.example.worldcanvas

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_canvas.*


class CanvasActivity : AppCompatActivity() {

    var element: Int = 0
    var colorPicker: PickColorDialog? = null
    private var position: Int = 0
    var currentColor = 0xf53703

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        element = intent.getIntExtra("Canvas", 0)
        position = intent.getIntExtra("Position", 0)
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putInt("Canvas", element)
        editor.apply()

        colorPicker = PickColorDialog(this)
        setContentView(R.layout.activity_canvas)
    }


    fun goToAR(view: View) {
        val intent = Intent(this, ArActivity::class.java)
        intent.putExtra("COLOR", canvas_view.brush.color)
        intent.putExtra("Object", element)
        intent.putExtra("Position", position)
        startActivity(intent)
    }

    fun pickColor(view: View) {
        colorPicker?.show()
    }


    fun changeColor(view: View) {
        colorPicker?.hide()
        canvas_view.brush.color = (view.background as ColorDrawable).color

    }

}
