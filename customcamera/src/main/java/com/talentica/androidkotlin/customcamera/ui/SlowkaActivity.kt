package com.talentica.androidkotlin.customcamera.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import butterknife.bindView
import com.talentica.androidkotlin.customcamera.R
import com.talentica.androidkotlin.customcamera.presenter.ActivityPresenter

abstract class SlowkaActivity <T>: AppCompatActivity() {

    protected val toolbar: Toolbar by bindView(R.id.toolbar)
    protected abstract val activityPresenter: ActivityPresenter<T>

    internal fun setContentViewWithToolbar(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupToolbar()
        setupStatusBarForLolipopDevices()
    }

    open internal fun setupToolbar() {
        setSupportActionBar(toolbar)
//        supportActionBar.setIcon(R.drawable.)
        supportActionBar?.title = null
    }

    private fun setupStatusBarForLolipopDevices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        }
    }

    internal fun attachPresenter(activityView: T,
                                 activity: Activity,
                                 savedInstanceState: Bundle?) {
        activityPresenter.attach(activityView, activity, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        activityPresenter.resume()
    }

    override fun onPause() {
        super.onPause()
        activityPresenter.pause()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
