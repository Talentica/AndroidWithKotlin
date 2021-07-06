package com.talentica.androidkotlin.sensors

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var compass: Compass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            compass = Compass(this)
        } catch (e:IllegalStateException) {
            e.printStackTrace()
            Toast.makeText(this, "Either accelerometer or magnetic sensor not found" , Toast.LENGTH_LONG).show()
        }
        compass?.arrowView = findViewById<ImageView>(R.id.main_image_hands)
    }

    override fun onResume() {
        super.onResume()
        compass?.start()
    }

    override fun onPause() {
        super.onPause()
        compass?.stop()
    }

    companion object {
        private val TAG = "CompassActivity"
    }

}

