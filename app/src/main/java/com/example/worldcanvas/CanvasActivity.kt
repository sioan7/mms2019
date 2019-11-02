package com.example.worldcanvas

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
//import kotlinx.android.synthetic.main.activity_canvas.*

class CanvasActivity : AppCompatActivity() {

    private var canvasView: CanvasView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        canvasView = CanvasView(this)
        setContentView(R.layout.activity_canvas)

    }


    fun goToAR(view: View) {
        val intent = Intent(this, ArActivity::class.java)
        intent.putExtra("COLOR", canvasView?.color ?: Color.MAGENTA)
        startActivity(intent)
    }
}
