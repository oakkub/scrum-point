package com.oakkub.pointpoker.helpers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle

/**
 * Created by oakkub on 8/12/2017 AD.
 */
class ShakeDetector(context: Context) : SensorEventListener {

    companion object {

        /*
         * The gForce that is necessary to register as shake.
         * Must be greater than 1G (one earth gravity unit).
         * You can install "G-Force", by Blake La Pierre
         * from the Google Play Store and run it to see how
         *  many G's it takes to register a shake
         */
        private const val SHAKE_THRESHOLD_GRAVITY = 2.7f

        private const val SHAKE_SLOP_TIME_MILLI_SECOND = 500L
        private const val SHAKE_COUNT_RESET_TIME_MILLI_SECOND = 3000L

    }

    var shakeTotalCount = 0
    var shakeTimeStamp = 0L
    var onShakeListener: ((Int) -> Unit)? = null

    val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    fun resume() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun pause() {
        sensorManager.unregisterListener(this, accelerometer)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val x = sensorEvent.values[0]
        val y = sensorEvent.values[1]
        val z = sensorEvent.values[2]
        val gravityEarth = SensorManager.GRAVITY_EARTH

        val gX = x / gravityEarth
        val gY = y / gravityEarth
        val gZ = z / gravityEarth

        val gXPowerOfTwo = (gX * gX).toDouble()
        val gYPowerOfTwo = (gY * gY).toDouble()
        val gZPowerOfTwo = (gZ * gZ).toDouble()

        // gForce will be close to 1 when there is no movement.
        val gForce = Math.sqrt(gXPowerOfTwo + gYPowerOfTwo + gZPowerOfTwo)

        if (gForce < SHAKE_THRESHOLD_GRAVITY) {
            return
        }

        val now = System.currentTimeMillis()

        // ignore shake events too close to each other
        if (shakeTimeStamp + SHAKE_SLOP_TIME_MILLI_SECOND > now) {
            return
        }

        // reset the shake count after SHAKE_COUNT_RESET_TIME_MILLI_SECOND of no shakes
        if (shakeTimeStamp + SHAKE_COUNT_RESET_TIME_MILLI_SECOND < now) {
            shakeTotalCount = 0
        }

        shakeTimeStamp = now
        shakeTotalCount++

        onShakeListener?.invoke(shakeTotalCount)
    }

}