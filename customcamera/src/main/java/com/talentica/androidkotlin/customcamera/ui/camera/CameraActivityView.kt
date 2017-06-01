package com.talentica.androidkotlin.customcamera.ui.camera

import com.talentica.androidkotlin.customcamera.presenter.camera.Ratio

interface CameraActivityView {

    fun setupCameraPreviewRatio(ratio: Ratio)

    fun setupSurfaceForCameraAndUnblock()

}