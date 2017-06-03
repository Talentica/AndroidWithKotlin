package com.talentica.androidkotlin.customcamera.presenter.camera

import android.hardware.Camera
import android.view.SurfaceHolder
import com.talentica.androidkotlin.customcamera.presenter.ActivityPresenter
import com.talentica.androidkotlin.customcamera.ui.camera.CameraActivityView

interface CameraActivityPresenter : ActivityPresenter<CameraActivityView> {

    fun cameraSurfaceReady(holder: SurfaceHolder)
    fun cameraSurfaceRefresh()
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray)
    fun switchOnFlash()
    fun switchOffFlash()
    fun clickPhoto()
}
