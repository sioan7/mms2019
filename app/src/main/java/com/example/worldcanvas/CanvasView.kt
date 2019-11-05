package com.example.worldcanvas

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.widget.AppCompatImageView
import android.graphics.*
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.DisplayMetrics
import android.util.Log
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.WindowManager


class CanvasView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatImageView(context) {

    val color = Color.MAGENTA

    private val path = Path()
    private val brush = Paint()
    private val pBackground = Paint()
    private val pText = Paint()

    val dm = DisplayMetrics()
    val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val lionBMP: Bitmap = BitmapFactory.decodeResource(resources, R.raw.lion_bmp).copy(Bitmap.Config.ARGB_8888, true)

    private val imagePosition: RectF

    private val floodFiller = QueueLinearFloodFiller(lionBMP)

    init {
        brush.isAntiAlias = true
        brush.color = color
        brush.style = Paint.Style.STROKE
        brush.strokeJoin = Paint.Join.ROUND
        brush.strokeWidth = 8f

        wm.defaultDisplay.getMetrics(dm)

        imagePosition = centerBitmapViewport(lionBMP, dm)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(lionBMP, null, imagePosition, brush)

    }

    private fun centerBitmapViewport(bitmap: Bitmap, dm: DisplayMetrics, threshold: Float = 100f): RectF{
        val scale = (dm.widthPixels - 2*threshold)/bitmap.width


        return RectF(
            threshold,
            dm.heightPixels/2 - bitmap.height*scale/2f,
            dm.widthPixels - threshold,
            dm.heightPixels/2f + bitmap.height*scale/2f
        )
    }
}