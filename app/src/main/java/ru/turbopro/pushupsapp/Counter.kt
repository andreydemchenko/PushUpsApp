package ru.turbopro.pushupsapp

import android.content.Context
import android.media.MediaPlayer
import android.widget.EditText
import android.widget.TextView
import java.util.*

class Counter(
    var counterTV: TextView,
    var typeET: TextView,
    var context: Context
) {
    var inProgress = false
    var away = false
    var count = 0
    var set: PushUpSet? = null
    lateinit var date: Date
    var trainingDays: ArrayList<TrainingDay>
    var tickPlayer: MediaPlayer
    private val trainingDay: TrainingDay

    init {
        inProgress = false
        away = false
        count = 0
        trainingDay = TrainingDay()
        trainingDays = trainingDay.loadTrainingDays(context)
        tickPlayer = MediaPlayer.create(context, R.raw.tick_sound)
    }

    fun start() {
        inProgress = true
        count = 0
        setCounter()
        date = Date()
        set = PushUpSet(typeET.text.toString(), date.time)
    }

    fun stop() {
        inProgress = false
        set?.setCount(count)
        set?.setEndTime(date.time)
        saveSet()
    }

    fun sensorNear() {
        if (away && inProgress) {
            away = false
            increaseCounter()
            setCounter()
            tickPlayer.start()
        }
    }

    fun sensorAway() {
        if (!away && inProgress) {
            away = true
        }
    }

    private fun increaseCounter() = count++

    private fun setCounter() {
        counterTV.text = count.toString()
    }

    fun close() {
        if (inProgress) {
            stop()
        }
    }

    fun saveSet() {
        addSetToTrainingDay()
        saveTrainingDays()
    }

    private fun addSetToTrainingDay() {
        val day: TrainingDay = trainingDays[trainingDays.size - 1]
        set?.let { day.addSet(it) }
    }

    private fun saveTrainingDays() {
        trainingDay.saveTrainingDays(context, trainingDays)
    }
}