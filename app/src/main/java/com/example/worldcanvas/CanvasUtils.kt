package com.example.worldcanvas

import android.graphics.drawable.VectorDrawable
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.Canvas
import android.graphics.Bitmap
import android.content.Context
import android.os.Build
import android.annotation.TargetApi
import androidx.core.content.ContextCompat


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.draw(canvas)
    return bitmap
}

fun getBitmap(context: Context, drawableId: Int): Bitmap {
    return when (val drawable = ContextCompat.getDrawable(context, drawableId)) {
        is BitmapDrawable -> BitmapFactory.decodeResource(context.resources, drawableId)
        is VectorDrawable -> getBitmap((drawable as VectorDrawable?)!!)
        else -> throw IllegalArgumentException("unsupported drawable type")
    }
}