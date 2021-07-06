package com.talentica.androidkotlin.customcamera.ui.camera

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.talentica.androidkotlin.customcamera.R
import com.talentica.androidkotlin.customcamera.dagger.HasComponent
import com.talentica.androidkotlin.customcamera.dagger.camera.CameraActivityComponent
import com.talentica.androidkotlin.customcamera.dagger.camera.CameraActivityComponentAssembler
import com.talentica.androidkotlin.customcamera.presenter.ActivityPresenter
import com.talentica.androidkotlin.customcamera.presenter.camera.CameraActivityPresenter
import com.talentica.androidkotlin.customcamera.ui.SlowkaActivity
import com.talentica.androidkotlin.customcamera.utils.Ratio
import kotterknife.bindView
import javax.inject.Inject

class CameraActivity : SlowkaActivity<CameraActivityView>(), CameraActivityView ,
        HasComponent<CameraActivityComponent?>, View.OnClickListener {

    @Inject
    protected lateinit var presenter: CameraActivityPresenter
    override var component: CameraActivityComponent? = null
    override val activityPresenter: ActivityPresenter<CameraActivityView>
        get() = presenter

    private val cameraContainer: FrameLayout by bindView(R.id.cameraContainer)
    private val flashButton: ImageButton by bindView(R.id.imb_flash)
    private val clickButton: ImageButton by bindView(R.id.imb_click)
    private val flashStatus: TextView by bindView(R.id.tv_flash_status)
    private var cameraView: CameraView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithToolbar(R.layout.activity_words_camera_view, false)
        setDaggerComponent(CameraActivityComponentAssembler.assemble(application))
        flashButton.setOnClickListener(this)
        clickButton.setOnClickListener(this)
    }

    private fun setDaggerComponent(component: CameraActivityComponent) {
        this.component = component
        this.component?.inject(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        attachPresenter(this, this, savedInstanceState)
    }

    override fun setupCameraPreviewRatio(ratio: Ratio) {
        val display = window.windowManager.defaultDisplay
        val params = cameraContainer.layoutParams?: FrameLayout.LayoutParams(0, 0)

        val width = ratio.getWidthInPortrait(display.width, display.height)
        val height = ratio.getHeightInPortrait(width)
        params.width = width
        params.height = height
        cameraContainer.layoutParams = params
    }

    override fun onPause() {
        super.onPause()
        tearDownCameraSurface()
    }

    override fun setupSurfaceForCameraAndUnblock() {
        cameraView = CameraView(this, presenter)
        cameraContainer.addView(cameraView)
    }

    private fun tearDownCameraSurface() {
        cameraContainer.removeView(cameraView)
        cameraView = null
    }

    override fun onClick(v: View?) {
        if (v?.id == flashButton.id) {
            if (flashStatus.text == "OFF") {
                flashStatus.text = "ON"
                presenter.switchOnFlash()
            } else {
                flashStatus.text = "OFF"
                presenter.switchOffFlash()
            }
        } else if (v?.id == clickButton.id) {
            presenter.clickPhoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, grantResults)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, CameraActivity::class.java)
        }
    }
}
