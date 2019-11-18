package com.example.worldcanvas

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class CanvasActivity : AppCompatActivity() {

    private var canvasView: CanvasView? = null
     var element: Int = 0
    private var position: Int = 0

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        element = intent.getIntExtra("Canvas",0)
        position = intent.getIntExtra("Position",0)
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putInt("Canvas",element)
        editor.apply()
        canvasView = CanvasView(this)
        setContentView(R.layout.activity_canvas)
    }


    fun goToAR(view: View) {
        val intent = Intent(this, ArActivity::class.java)
        intent.putExtra("COLOR", canvasView?.color ?: Color.MAGENTA)
        intent.putExtra("Object",element)
        intent.putExtra("Position",position)
        startActivity(intent)
    }


}
