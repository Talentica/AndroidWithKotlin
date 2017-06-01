package com.talentica.androidkotlin.customcamera.presenter.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.SurfaceHolder
import com.talentica.androidkotlin.customcamera.model.camera.CameraActivityModel
import com.talentica.androidkotlin.customcamera.ui.camera.CameraActivityView

class CameraActivityPresenterImpl constructor(val cameraActivityModel: CameraActivityModel)
        : CameraActivityPresenter {

    private val CAMERA_REQUEST_PERMISSION: Int = 123;
    private lateinit var cameraActivityView: CameraActivityView
    private lateinit var activity: Activity

    override fun attach(activityView: CameraActivityView,
                        activity: Activity,
                        savedInstanceState: Bundle?) {
        this.cameraActivityView = activityView
        this.activity = activity
        cameraActivityView.setupCameraPreviewRatio(Ratio.FOUR_TO_THREE)
    }

    override fun resume() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            activity.requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_PERMISSION)
        } else {
            startCameraComponents()
        }
    }

    override fun pause() {
        cameraActivityModel.stopProcessingPreview()
    }

    override fun cameraSurfaceReady(holder: SurfaceHolder) {
        cameraActivityModel.cameraSurfaceReady(holder)
    }

    override fun cameraSurfaceRefresh() {
        cameraActivityModel.cameraSurfaceRefresh()
    }

    override fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == CAMERA_REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraComponents()
            }
        }
    }

    private fun startCameraComponents() {
        cameraActivityModel.startProcessingPreview(this)
        cameraActivityView.setupSurfaceForCameraAndUnblock()
    }
}
