package com.example.worldcanvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.widget.AppCompatImageView
import com.caverock.androidsvg.SVG


class CanvasView
@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatImageView(context) {

    val color = Color.MAGENTA

    private var params: LayoutParams
    private val path = Path()
    private val brush = Paint()

//    private val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//    private val drawableLion = ContextCompat.getDrawable(context, R.drawable.im_lion)
//    private val bmLion = BitmapFactory.decodeResource(context.resources, R.drawable.im_lion)

//    private val src = Rect(0, 0, bmLion.width, bmLion.height)
//    private val dst = Rect(0, 0, 100, 100)

    //    private val lionSVG = SVG.getFromResource(resources, R.raw.lion_v)
    private val lionSVG = SVG.getFromResource(resources, R.raw.lion_v)
    private val imagePosition = RectF(0f, 0f, 100f, 100f)



//    private val bmLion = getBitmap(context, R.raw.lion2)

//    private val lionSVG = SVGBuilder().readFromResource(resources, R.drawable.im_lion2).build()

//    private val bmLion2 = BitmapFactory.decodeResource(resources, R.drawable.im_lion2)

//    private val lion: Drawable = context.resources.getDrawable(R.drawable.im_lion2)

    init {

        brush.isAntiAlias = true
        brush.color = color
        brush.style = Paint.Style.STROKE
        brush.strokeJoin = Paint.Join.ROUND
        brush.strokeWidth = 8f

        params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

//        this.setImageBitmap(bitmap)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointX = event.x
        val pointY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(pointX, pointY)
                return true
            }

            MotionEvent.ACTION_MOVE -> path.lineTo(pointX, pointY)

            else -> return false
        }

        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, brush)

//        lion.draw(canvas)
//        canvas.drawPicture(lionSVG.picture)
//        lionSVG.renderToCanvas(canvas)
        val left: Float = (width - lionSVG.documentWidth)/2
        val top: Float = (height - lionSVG.documentHeight)/2
        imagePosition.left = left
        imagePosition.top = top
        lionSVG.renderToCanvas(canvas, imagePosition)

//        canvas.drawBitmap(bmLion, src, dst, brush)
//        drawableLion?.draw(canvas)
    }


}
