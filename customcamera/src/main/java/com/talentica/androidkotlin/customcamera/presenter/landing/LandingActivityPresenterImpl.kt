package com.talentica.androidkotlin.customcamera.presenter.landing

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.MenuItem
import android.widget.GridView
import android.widget.Toast
import com.talentica.androidkotlin.customcamera.R
import com.talentica.androidkotlin.customcamera.adapter.landing.LandingGalleryAdapter
import com.talentica.androidkotlin.customcamera.ui.landing.LandingActivityView
import com.talentica.androidkotlin.customcamera.utils.Utils
import androidx.core.content.FileProvider
import android.webkit.MimeTypeMap


class LandingActivityPresenterImpl : LandingActivityPresenter {

    private lateinit var landingActivityView: LandingActivityView
    private lateinit var activity: Activity
    private lateinit var landingAdapter:LandingGalleryAdapter
    private val STORAGE_PERMISSION = 1212

    override fun attach(activityView: LandingActivityView, activity: Activity, savedInstanceState: Bundle?) {
        this.landingActivityView = activityView
        this.activity = activity
        landingAdapter = LandingGalleryAdapter(activity)
        activityView.setAdapter(landingAdapter)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.nav_gallery) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(
                    "content://media/internal/images/media"))
            activity.startActivity(intent)
        }

        landingActivityView.closeDrawer()
        return true;
    }

    override fun resume() {
        checkStoragePermission()
    }

    override fun pause() {
    }

    fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //File write logic here
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION);
                    // STORAGE_PERMISSION is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults != null && grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "Thanks", Toast.LENGTH_SHORT).show()
                    Utils().checkAndMakeDir()
                }
            }
        }
    }

    override fun addListOfPicsToAdapter(gridView: GridView) {
        landingAdapter.refreshFilesFromFolder()
    }

    override fun lauchPhotoPreview(position: Int) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val files = Utils().storageDir.listFiles()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val file = files.get(position)
            val photoURI = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file)
            val ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
            intent.setDataAndType(photoURI, type)
        } else {
            intent.setDataAndType(Uri.parse("file://" + landingAdapter.getItem(position)), "image/*")
        }
        activity.startActivity(intent)
    }
}
