package ru.turbopro.pushupsapp

import android.content.Context
import androidx.preference.PreferenceManager
import android.text.format.DateFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class TrainingDay() {
    private var goal = 0
    private var setsList: ArrayList<PushUpSet> = ArrayList()
    private var date: String
    private var dateTime: Long = 0
    private var progress = 0
    private var lastGoal = 50
    private val DAY_IN_MILLIS = 1000 * 60 * 60 * 24

    init {
        dateTime = Date().time
        date = DateFormat.format("dd/MM/yyyy", dateTime) as String
        goal = lastGoal
        progress = 0
    }

    fun loadTrainingDays(context: Context): ArrayList<TrainingDay> {
        var trainingDays = ArrayList<TrainingDay>()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val jsonGet = prefs.getString("trainingDays", null)
        val type: Type = object : TypeToken<ArrayList<TrainingDay>>() {}.type
        if (gson.fromJson<ArrayList<TrainingDay>>(jsonGet, type) != null) trainingDays = gson.fromJson(jsonGet, type)
        val day: TrainingDay
        if (trainingDays.size != 0) {
            day = trainingDays[trainingDays.size - 1]
            if (!day.isToday()) {
                trainingDays.add(TrainingDay())
            }
        } else trainingDays.add(TrainingDay())
        return fillMissingDays(trainingDays)
    }

    fun saveTrainingDays(context: Context, trainingDays: ArrayList<TrainingDay>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val gson = Gson()
        val jsonSet: String = gson.toJson(trainingDays)
        editor.putString("trainingDays", jsonSet)
        editor.apply() // This line is IMPORTANT !!!
    }

    fun fillMissingDays(trainingDays: ArrayList<TrainingDay>): ArrayList<TrainingDay> {
        var size = trainingDays.size
        val today = trainingDays[size - 1]
        if (size > 1) {
            var secondLastDay = trainingDays[trainingDays.size - 2]
            while (!isYesterday(secondLastDay, today)) {
                val time = secondLastDay.getDateTime() + DAY_IN_MILLIS
                secondLastDay = TrainingDay()
                secondLastDay.setDateTime(time)
                trainingDays.add(size - 2, secondLastDay)
                size++
            }
        }
        if (size < 7) {
            val firstDay = trainingDays[0]
            var time = firstDay.getDateTime() - DAY_IN_MILLIS
            while (size < 7) {
                val day = TrainingDay()
                day.setDateTime(time)
                trainingDays.add(0, day)
                time -= DAY_IN_MILLIS.toLong()
                size++
            }
        }
        return trainingDays
    }

    private fun isYesterday(yesterday: TrainingDay, today: TrainingDay): Boolean {
        return DateFormat.format("dd/MM/yyyy", yesterday.getDateTime()) as String ==
                DateFormat.format("dd/MM/yyyy", today.getDateTime() - DAY_IN_MILLIS) as String
    }

    fun getDateTime(): Long = dateTime

    fun setDateTime(time: Long) {
        dateTime = time
        date = DateFormat.format("dd/MM/yyyy", dateTime) as String
    }

    fun getGoal(): Int = goal

    fun setGoal(goal: Int) {
        this.goal = goal
        lastGoal = goal
    }

    fun getSetsList(): ArrayList<PushUpSet> = setsList

    fun addSet(set: PushUpSet) {
        setsList.add(set)
        progress += set.getCount()
    }

    fun isToday(): Boolean {
        return date == DateFormat.format("dd/MM/yyyy", Date().time) as String
    }

    fun getProgressPercent(): Float {
        return (100 * progress / goal).toFloat()
    }

    fun getProgress(): Int = progress

    fun getDate(): String = date
}