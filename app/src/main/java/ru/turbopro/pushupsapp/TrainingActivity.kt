package ru.turbopro.pushupsapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TrainingActivity : AppCompatActivity() {
    private lateinit var counterTV: TextView
    private lateinit var startStopTV: TextView
    private lateinit var mySensorManager: SensorManager
    private lateinit var myProximitySensor: Sensor
    private lateinit var counter: Counter
    private lateinit var typeET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)
        counterTV = findViewById(R.id.progressTV)
        startStopTV = findViewById(R.id.startStopTV)
        typeET = findViewById(R.id.typeET)
        createProximitySensorListener()
        counter = Counter(counterTV, typeET, this.applicationContext)
    }

    override fun onPause() {
        super.onPause()
        counter.close()
    }

    private fun createProximitySensorListener() {
        mySensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        val proximitySensorEventListener: SensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                    if (event.values[0] == 0F) counter.sensorNear() else counter.sensorAway()
                }
            }
        }
        mySensorManager.registerListener(proximitySensorEventListener,
            myProximitySensor,
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun startCounter() {
        if (startStopTV.text.toString() == getString(R.string.start)) {
            startStopTV.text = getString(R.string.stop)
            counter.start()
        } else {
            startStopTV.text = getString(R.string.start)
            counter.stop()
        }
    }

    fun close() {
        finish()
    }
}