package com.talentica.androidkotlin.customcamera.model.camera

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.hardware.Camera
import android.os.Handler
import android.view.SurfaceHolder
import com.talentica.androidkotlin.customcamera.callbacks.PhotoClickedCallback
import com.talentica.androidkotlin.customcamera.utils.Ratio
import com.talentica.androidkotlin.customcamera.utils.Utils
import rx.Observable
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CameraAdapter constructor() : Camera.PictureCallback {

    companion object {
        private val ROTATION_DEGREE = 90
        private val MIN_AUTO_FOCUS_COUNT = 1
        private val NEXT_FOCUS_DELAY = 1000L
        private val FOCUS_MODE = "macro"
        private val ORIENTATION_KEY = "orientation"
        private val ORIENTATION_VALUE = "landscape"
        private val FLASH_ON = "torch"
        private val FLASH_OFF = "off"
    }

    protected lateinit var context: Context
    
    private val isFocusedOnTarget: Boolean
        get() = focusedCountInRow >= MIN_AUTO_FOCUS_COUNT
    private var cameraSizeRect: Rect = Rect(0,0,0,0)
    private var isFlashActive = false
    private var focusedCountInRow: Int = 0
    private var active = false
    private var initialized = false

    private val camera: Camera
        get() = cameraHandle?:throw AssertionError("Camera was not initialized correctly")
    private var cameraHandle: Camera? = null
    private var photoCallback:PhotoClickedCallback? = null

    fun start(): Observable<ByteArray> {
        if (!initialized) {
            initCamera()
        }
        camera.startPreview()
        active = true
        refreshPreviewSizeRect()
        return getObservableOnCameraFrames()
    }

    private fun getObservableOnCameraFrames(): Observable<ByteArray> {
        return Observable.create { subscriber ->
            camera.setPreviewCallback { cameraFrame, camera ->
                if (!subscriber.isUnsubscribed && isFocusedOnTarget) {
                    subscriber.onNext(cameraFrame)
                }
            }
        }
    }

    private fun initCamera() {
        cameraHandle = Camera.open()
        configure(Ratio.FOUR_TO_THREE)
        initialized = true
    }

    fun stop() {
        if (active) {
            active = false
            isFlashActive = false
            initialized = false
            tearDownCamera()
        }
    }

    fun takePicture(photoCallback: PhotoClickedCallback) {
        this.photoCallback = photoCallback
        camera.takePicture(null, null, this)
    }

    override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
        val pictureFile = getOutputMediaFile() ?: return
        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close()
        } catch (e: FileNotFoundException) {
            photoCallback?.photoClickFilure()
            e.printStackTrace()
            return
        } catch (e: IOException) {
            photoCallback?.photoClickFilure()
            e.printStackTrace()
            return
        }
        photoCallback?.photoClickSuccess()
    }

    private fun getOutputMediaFile(): File? {
        val mediaStorageDir = Utils().checkAndMakeDir()
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(Date())
        val mediaFile: File
        mediaFile = File(mediaStorageDir?.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg")

        return mediaFile
    }

    private fun tearDownCamera() {
        camera.stopPreview()
        camera.setPreviewCallback(null)
        camera.release()
        cameraHandle = null
    }

    fun setupSurface(surfaceHolder: SurfaceHolder) {
        camera.setPreviewDisplay(surfaceHolder)
    }

    private fun configure(ratio: Ratio) {
        val params = camera.parameters
        configPreviewRatioAndSize(params, ratio)
        configPictureRatioAndSize(params, ratio)
        configRotation(params)
        configFocusMode(params)
        configOrientation(params)
        camera.parameters = params
    }

    fun hasFlash(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    fun toggleFlash() {
        isFlashActive = !isFlashActive
        if (isFlashActive) {
            setFlashModeIfCameraStillAvailable(FLASH_ON)
        } else {
            setFlashModeIfCameraStillAvailable(FLASH_OFF)
        }
    }

    private fun setFlashModeIfCameraStillAvailable(mode: String) {
        val params = cameraHandle?.parameters
        params?.flashMode = mode
        cameraHandle?.parameters = params
    }

    private fun configOrientation(params: Camera.Parameters) {
        params.set(ORIENTATION_KEY, ORIENTATION_VALUE)
    }

    private fun configFocusMode(params: Camera.Parameters) {
        params.focusMode = FOCUS_MODE
    }

    private fun configRotation(params: Camera.Parameters) {
        params.setRotation(ROTATION_DEGREE)
        camera.setDisplayOrientation(ROTATION_DEGREE)
    }

    private fun configPreviewRatioAndSize(params: Camera.Parameters, ratio: Ratio) {
        val selected = ratio.extractFourByThreeSize(camera.parameters?.supportedPreviewSizes!!)
        params.setPreviewSize(selected.width, selected.height)
    }

    private fun configPictureRatioAndSize(params: Camera.Parameters, ratio: Ratio) {
        val selected = ratio.extractFourByThreeSize(camera.parameters?.supportedPictureSizes!!)
        params.setPictureSize(selected.width, selected.height)
    }

    fun autoFocusOnTargetAndRepeat() {
        camera.autoFocus { wasSuccessful, camera ->
            if (wasSuccessful) {
                ++focusedCountInRow
            } else {
                focusedCountInRow = 0
            }
            repeatAutoFocusOnTargetWithDelay()
        }
    }

    private fun repeatAutoFocusOnTargetWithDelay() {
        Handler().postDelayed({
            if (active) {
                camera.cancelAutoFocus()
                autoFocusOnTargetAndRepeat()
            }
        }, NEXT_FOCUS_DELAY)
    }

    fun refreshPreviewSizeRect() {
        val previewSize = camera.parameters?.previewSize?:throw AssertionError("could not read camera size")
        cameraSizeRect = Rect(0, 0, previewSize.width, previewSize.height)
    }
}