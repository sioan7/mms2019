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

private val imgSize = 256
private val imgSizeBound = imgSize - 1

fun createPattern(colors: List<Int>, rule: Int): Bitmap {
    val bmp = Bitmap.createBitmap(imgSize, imgSize, Bitmap.Config.ARGB_8888)
    bmp.setHasAlpha(false)
    val imageData = when (rule) {
        1 -> rule1(colors)
        2 -> rule2(colors)
        else -> rule1(colors)
    }.reduce { acc, ints -> acc + ints }.toIntArray()
    bmp.setPixels(imageData, 0, imgSize, 0, 0, imgSize, imgSize)
    bmp.isPremultiplied = true
    return bmp
}

fun rule1(colors: List<Int>): Array<Array<Int>> {
    val array = Array(imgSize) { Array(imgSize) { 0 } }
    val size = colors.size
    for (i in 0..imgSizeBound) {
        for (j in 0..imgSizeBound) {
            if (i < imgSize / 2) {
                if (j < imgSize / 2) {
                    array[i][j] = colors[0]
                } else {
                    array[i][j] = colors[1 % size]
                }
            } else {
                if (j < imgSize / 2) {
                    array[i][j] = colors[2 % size]
                } else {
                    array[i][j] = colors[3 % size]
                }
            }
        }
    }
    return array
}

fun rule2(colors: List<Int>): Array<Array<Int>> {
    val array = Array(imgSize) { Array(imgSize) { 0 } }
    val size = colors.size
    for (i in 0..imgSizeBound) {
        for (j in 0..imgSizeBound) {
            array[i][j] = colors[(i + j) % size]
        }
    }
    return array
}

fun firstCreatePattern(colors: List<Int>): Bitmap {
    val bmp = Bitmap.createBitmap(imgSize, imgSize, Bitmap.Config.ARGB_8888)
    bmp.setHasAlpha(false)
    for (x in 0..imgSizeBound) {
        for (y in 0..imgSizeBound) {
            bmp.setPixel(y, x, colors[x % colors.size])
        }
    }
    bmp.isPremultiplied = true
    return bmp
}
