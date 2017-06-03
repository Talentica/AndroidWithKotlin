package com.talentica.androidkotlin.customcamera.ui.camera

import com.talentica.androidkotlin.customcamera.utils.Ratio

interface CameraActivityView {

    fun setupCameraPreviewRatio(ratio: Ratio)

    fun setupSurfaceForCameraAndUnblock()

}