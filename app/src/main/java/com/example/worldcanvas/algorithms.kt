package com.example.worldcanvas

import android.graphics.Bitmap
import android.graphics.Point
import java.util.*


fun floodFill(image: Bitmap, target: Point, targetColor: Int, replacementColor: Int) {
    val width = image.width
    val height = image.height
    if (targetColor != replacementColor) {
        val queue = LinkedList<Point>()
        queue.add(target)
        do {
            val node = queue.poll()
            var x = node.x
            val y = node.y
            while (x > 0 && image.getPixel(x - 1, y) == targetColor) {
                x--
            }
            var spanUp = false
            var spanDown = false
            while (x < width && image.getPixel(x, y) == targetColor) {
                image.setPixel(x, y, replacementColor)
                if (!spanUp && y > 0 && image.getPixel(x, y - 1) == targetColor) {
                    queue.add(Point(x, y - 1))
                    spanUp = true
                } else if (spanUp && y > 0 && image.getPixel(x, y - 1) != targetColor) {
                    spanUp = false
                }
                if (!spanDown && y < height - 1 && image.getPixel(x, y + 1) == targetColor) {
                    queue.add(Point(x, y + 1))
                    spanDown = true
                } else if (spanDown && y < height - 1 && image.getPixel(x, y + 1) != targetColor) {
                    spanDown = false
                }
                x++
            }
        } while (queue.isNotEmpty())
    }
}

fun createPattern(colors: List<Int>): Bitmap {
    val bmp = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
    bmp.setHasAlpha(false)
    for (x in 0..511) {
        for (y in 0..511) {
            bmp.setPixel(y, x, colors[x % colors.size])
        }
    }
    bmp.isPremultiplied = true
    return bmp
}
