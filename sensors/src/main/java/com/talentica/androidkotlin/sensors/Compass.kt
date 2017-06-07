package com.talentica.androidkotlin.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView

class Compass(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager
    private val gsensor: Sensor
    private val msensor: Sensor
    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private var azimuth = 0f
    private var currectAzimuth = 0f

    // compass arrow to rotate
    var arrowView: ImageView? = null

    init {
        sensorManager = context
                .getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    fun start() {
        sensorManager.registerListener(this, gsensor,
                SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, msensor,
                SensorManager.SENSOR_DELAY_GAME)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val alpha = 0.97f

        synchronized(this) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]
                // mGravity = event.values;
//                Log.d(TAG, "GravityRaw: "+event.values.contentToString()+ " filtered: "+mGravity.contentToString());
            }

            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                // mGeomagnetic = event.values;
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0]
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1]
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2]
//                Log.d(TAG,"MagneticRaw: "+event.values.contentToString()+ " Filtered: "+mGeomagnetic.contentToString());
            }

            val R = FloatArray(9)
            val I = FloatArray(9)
            val success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)
//            Log.d(TAG,"Gravity: "+mGravity.contentToString()+ " Magnetic: "+mGeomagnetic.contentToString()
//            + " R: "+ R.contentToString() +" I: "+ I.contentToString());
            if (success) {
//                Log.d(TAG,"Gravity: "+mGravity.contentToString()+ " Magnetic: "+mGeomagnetic.contentToString())
                Log.d(TAG," R: "+ R.contentToString() +" I: "+ I.contentToString());
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                // Log.d(TAG, "azimuth (rad): " + azimuth);
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat() // orientation
                azimuth = (azimuth + 360) % 360
                // Log.d(TAG, "azimuth (deg): " + azimuth);
                adjustArrow()
            }
        }
    }

    private fun adjustArrow() {
        if (arrowView == null) {
            Log.i(TAG, "arrow view is not set")
            return
        }

//        Log.i(TAG, "will set rotation from " + currectAzimuth + " to " + azimuth)
        val an = RotateAnimation(-currectAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f)
        currectAzimuth = azimuth

        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true

        arrowView!!.startAnimation(an)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    companion object {
        private val TAG = "Compass"
    }
}
