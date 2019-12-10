package com.example.worldcanvas

import android.graphics.Bitmap


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
