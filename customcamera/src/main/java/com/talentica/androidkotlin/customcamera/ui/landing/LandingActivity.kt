package com.talentica.androidkotlin.customcamera.ui.landing

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.talentica.androidkotlin.customcamera.R
import com.talentica.androidkotlin.customcamera.adapter.landing.LandingGalleryAdapter
import com.talentica.androidkotlin.customcamera.dagger.HasComponent
import com.talentica.androidkotlin.customcamera.dagger.landing.LandingActivityComponent
import com.talentica.androidkotlin.customcamera.dagger.landing.LandingActivityComponentAssembler
import com.talentica.androidkotlin.customcamera.presenter.ActivityPresenter
import com.talentica.androidkotlin.customcamera.presenter.landing.LandingActivityPresenter
import com.talentica.androidkotlin.customcamera.ui.SlowkaActivity
import com.talentica.androidkotlin.customcamera.ui.camera.CameraActivity
import kotterknife.bindView
import javax.inject.Inject


class LandingActivity : SlowkaActivity<LandingActivityView>(), LandingActivityView,
        HasComponent<LandingActivityComponent?>, AdapterView.OnItemClickListener {

    @Inject
    protected lateinit var presenter: LandingActivityPresenter
    override var component: LandingActivityComponent? = null
    override val activityPresenter: ActivityPresenter<LandingActivityView>
        get() = presenter

    val navigationView: NavigationView by bindView(R.id.nav_view)
    val gridView: GridView by bindView(R.id.gridview)
    val emptyView: TextView by bindView(R.id.emptyview)
    val drawer: DrawerLayout by bindView(R.id.drawer_layout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewWithToolbar(R.layout.activity_landing, true)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity(CameraActivity.createIntent(this))
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        gridView.emptyView = emptyView

        setDaggerComponent(LandingActivityComponentAssembler.assemble(application))

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        attachPresenter(this, this, savedInstanceState)
        navigationView.setNavigationItemSelectedListener(presenter)
    }

    override fun setAdapter(adapter: LandingGalleryAdapter) {
        gridView.setAdapter(adapter)
        gridView.setOnItemClickListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.lauchPhotoPreview(position)
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
        presenter.addListOfPicsToAdapter(gridView)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun setDaggerComponent(component: LandingActivityComponent) {
        this.component = component
        this.component?.inject(this)
    }

    override fun onBackPressed() {
        closeDrawer()
        super.onBackPressed()
    }

    override fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }
}
