package com.example.worldcanvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.scale


class CanvasView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatImageView(context, attributeSet) {

    private val path = Path()
    val brush = Paint()
    val INITIAL_COLOR = Color.WHITE
    private val NEUTRAL_WHITE = Color.valueOf(250f / 255f, 250f / 255f, 250f / 255f).toArgb()
    private val NEUTRAL_WHITE2 = Color.valueOf(244f / 255f, 244f / 255f, 244f / 255f).toArgb()
    private val pBackground = Paint()
    private val pText = Paint()

    private val dm = DisplayMetrics()
    private val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    var bmpImage: Bitmap = BitmapFactory
        .decodeResource(resources, context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE).getInt("Canvas",0))
        .copy(Bitmap.Config.ARGB_8888, true)
    private var bmpModel: Bitmap = BitmapFactory
        .decodeResource(resources, context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE).getInt("Model",0))
        .copy(Bitmap.Config.ARGB_8888, true)
    private var imagePosition: RectF
    private var modelPosition: RectF

    init {
        brush.isAntiAlias = true
        brush.color = INITIAL_COLOR
        brush.style = Paint.Style.STROKE
        brush.strokeJoin = Paint.Join.ROUND
        brush.strokeWidth = 8f

        wm.defaultDisplay.getMetrics(dm)

        imagePosition = centerBitmapViewport(bmpImage, dm)
        modelPosition = topRightModelViewport(bmpImage, dm)

        bmpImage = bmpImage.scale(
            (imagePosition.right - imagePosition.left).toInt(),
            (imagePosition.bottom - imagePosition.top).toInt()
        )

        bmpModel = bmpModel.scale(
            (modelPosition.right - modelPosition.left).toInt(),
            (modelPosition.bottom - modelPosition.top).toInt()
        )


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bmpModel, null, modelPosition, brush)
        canvas.drawBitmap(bmpImage, null, imagePosition, brush)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = (event.x - imagePosition.left).toInt()
        val y = (event.y - imagePosition.top).toInt()
        if (x < 0 || x >= bmpImage.width || y < 0 || y >= bmpImage.height) return false
        if (bmpImage.getPixel(x, y) == NEUTRAL_WHITE) return false
        if (bmpImage.getPixel(x, y) == NEUTRAL_WHITE2) return false
        if (bmpImage.getPixel(x, y) == Color.BLACK) return false
        val avgColor = (Color.valueOf(bmpImage.getPixel(x, y))).let {
            (it.red() + it.blue() + it.green()) / 3
        }
        if (avgColor < .1) return false
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val floodFiller = QueueLinearFloodFiller(bmpImage, bmpImage.getPixel(x, y), brush.color)
                floodFiller.setTolerance(10)
                floodFiller.floodFill(x, y)
                postInvalidate()
                true
            }
            else -> false
        }
    }

    private fun centerBitmapViewport(bitmap: Bitmap, dm: DisplayMetrics, threshold: Float = 100f): RectF {
         val scalingFactor = (dm.widthPixels - 2 * threshold) / bitmap.width / 2
         return RectF(
             threshold,
             dm.heightPixels / 2 - bitmap.height * scalingFactor,
             dm.widthPixels - threshold,
             dm.heightPixels / 2 + bitmap.height * scalingFactor
         )
    }

    private fun topRightModelViewport(bitmap: Bitmap, dm: DisplayMetrics, threshold: Float = 20f): RectF {
        val scalingFactor = 300f / bitmap.height / 2f

        return RectF(
            dm.widthPixels / 2 - bitmap.width * scalingFactor,
            threshold,
            dm.widthPixels / 2 + bitmap.width * scalingFactor,
            threshold + 300f
        )
    }



}



