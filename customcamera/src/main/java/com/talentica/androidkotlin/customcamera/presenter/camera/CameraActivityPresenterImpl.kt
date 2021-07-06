package com.talentica.androidkotlin.customcamera.presenter.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.SurfaceHolder
import android.widget.Toast
import com.talentica.androidkotlin.customcamera.callbacks.PhotoClickedCallback
import com.talentica.androidkotlin.customcamera.model.camera.CameraActivityModel
import com.talentica.androidkotlin.customcamera.ui.camera.CameraActivityView
import com.talentica.androidkotlin.customcamera.utils.Ratio


class CameraActivityPresenterImpl constructor(val cameraActivityModel: CameraActivityModel) : CameraActivityPresenter, PhotoClickedCallback {

    private val CAMERA_REQUEST_PERMISSION: Int = 123;
    private lateinit var cameraActivityView: CameraActivityView
    private lateinit var activity: Activity
    private var isPermissionGranted = false

    override fun attach(activityView: CameraActivityView,
                        activity: Activity,
                        savedInstanceState: Bundle?) {
        this.cameraActivityView = activityView
        this.activity = activity
        cameraActivityView.setupCameraPreviewRatio(Ratio.FOUR_TO_THREE)
    }

    override fun resume() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            isPermissionGranted = true
            startCameraComponents()
        }
    }

    private fun requestCameraPermission() {
        // Here, thisActivity is the current activity
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_PERMISSION);
            // MY_PERMISSIONS_REQUEST_CAMERA is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    override fun switchOnFlash() {
        if (!isPermissionGranted ) {
            return
        }
        cameraActivityModel.toggleFlash()
    }

    override fun switchOffFlash() {
        if (!isPermissionGranted ) {
            return
        }
        cameraActivityModel.toggleFlash()
    }

    override fun clickPhoto() {
        cameraActivityModel.clickPhoto(this)
    }

    override fun photoClickSuccess() {
        Toast.makeText(activity, "Refreshing Gallery", Toast.LENGTH_SHORT).show()
        activity.finish()
    }

    override fun photoClickFilure() {
        Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show()
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
                isPermissionGranted = true
                //works as refresh for camera view
                activity.recreate()
            }
        }
    }

    private fun startCameraComponents() {
        cameraActivityModel.startProcessingPreview(this)
        cameraActivityView.setupSurfaceForCameraAndUnblock()
    }
}
