package com.talentica.androidkotlin.customcamera.model.camera

import android.app.Activity
import android.view.SurfaceHolder
import com.talentica.androidkotlin.customcamera.callbacks.PhotoClickedCallback
import com.talentica.androidkotlin.customcamera.presenter.camera.CameraActivityPresenterImpl
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions

class CameraActivityModel constructor(val cameraAdapter: CameraAdapter) {

    protected var cameraFrameSubscription = Subscriptions.unsubscribed()
    private lateinit var cameraActivityPresenter: CameraActivityPresenterImpl

    fun startProcessingPreview(cameraActivityPresenter: CameraActivityPresenterImpl) {
        this.cameraActivityPresenter = cameraActivityPresenter
        observeFrames(cameraAdapter.start())
    }

    fun stopProcessingPreview() {
        stopPreviewing()
    }

    private fun observeFrames(cameraFrameObservable: Observable<ByteArray>) {
        cameraFrameSubscription.unsubscribe()
        cameraFrameSubscription = cameraFrameObservable
                .onBackpressureLatest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { extractedDocument ->
                }
    }

    private fun stopPreviewing() {
        cameraFrameSubscription.unsubscribe()
        cameraAdapter.stop()
    }

    fun cameraSurfaceReady(holder: SurfaceHolder) {
        cameraAdapter.setupSurface(holder)
        cameraAdapter.autoFocusOnTargetAndRepeat()
    }

    fun cameraSurfaceRefresh() {
        observeFrames(cameraAdapter.start())
    }

    fun toggleFlash() {
        cameraAdapter.toggleFlash()
    }

    fun clickPhoto(photoCallback:PhotoClickedCallback) {
        cameraAdapter.takePicture(photoCallback)
    }
}
