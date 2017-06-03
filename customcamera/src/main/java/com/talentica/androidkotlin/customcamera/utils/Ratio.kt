package com.talentica.androidkotlin.customcamera.utils

import android.hardware.Camera

enum class Ratio(private val ratio: Float) {
    FOUR_TO_THREE(4f / 3f);

    fun getWidthInPortrait(width: Int, height: Int): Int {
        if (ratio * width <= height) {
            return width.toInt()
        } else {
            return (1f / ratio * width).toInt()
        }
    }

    fun getHeightInPortrait(width: Int): Int {
        return (ratio * width).toInt()
    }

    fun extractFourByThreeSize(sizes: List<Camera.Size>) : Camera.Size {
        val betterSizes = sizes.filter { size ->
            val sizeRatio = (size.width).toFloat() / (size.height).toFloat()
            Math.abs(sizeRatio - ratio) < 0.001
        }.sortedBy { size -> size.width }
        if (betterSizes.isEmpty())
            throw AssertionError("could not pick 3/4 camera")

        return betterSizes.find { it.width > 2000 } ?: betterSizes.last()
    }
}