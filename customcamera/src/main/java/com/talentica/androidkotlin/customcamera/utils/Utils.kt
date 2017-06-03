package com.talentica.androidkotlin.customcamera.utils

import android.os.Environment
import android.util.Log
import java.io.File

/**
 * Created by suyashg on 03/06/17.
 */
class Utils {
    val storageDir = File(Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CustomCamera")

    fun checkAndMakeDir():File? {
        val mediaStorageDir = storageDir
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CustomCameraApp", "failed to create directory")
                return null
            }
        }
        return mediaStorageDir
    }
}