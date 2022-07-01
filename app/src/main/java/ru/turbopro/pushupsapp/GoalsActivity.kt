package ru.turbopro.pushupsapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GoalsActivity : AppCompatActivity() {
    private lateinit var goalTV: TextView
    private var goalValue = 0
    private lateinit var trainingDays: ArrayList<TrainingDay>
    private lateinit var lastTrainingDay: TrainingDay
    private lateinit var progressPercentTV: TextView
    private lateinit var progressRatioTV: TextView
    private lateinit var passTV: TextView
    private val trainingDay = TrainingDay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
        goalTV = findViewById(R.id.goalTV)
        progressPercentTV = findViewById(R.id.progressTV)
        progressRatioTV = findViewById(R.id.progressRatioTV)
        passTV = findViewById(R.id.passTV)
        trainingDays = trainingDay.loadTrainingDays(this)
        lastTrainingDay = trainingDays[trainingDays.size - 1]
        loadGoal()
    }

    override fun onPause() {
        super.onPause()
        trainingDay.saveTrainingDays(this, trainingDays)
    }

    fun close() {
        finish()
    }

    fun decreaseGoal() {
        goalValue -= 5
        setGoalUpdatePass()
    }

    fun increaseGoal() {
        goalValue += 5
        setGoalUpdatePass()
    }

    private fun loadGoal() {
        goalValue = lastTrainingDay.getGoal()
        setGoalUpdatePass()
    }

    private fun setGoalUpdatePass() {
        goalTV.text = goalValue.toString()
        lastTrainingDay.setGoal(goalValue)
        setProgressTV()
        updatePass()
    }

    private fun setProgressTV() {
        progressPercentTV.text = lastTrainingDay.getProgressPercent().toString() + "%"
        progressRatioTV.text =
            lastTrainingDay.getProgress().toString() + "/" + lastTrainingDay.getGoal()
    }

    private fun updatePass() {
        val pass = calculatePass()
        passTV.text = pass.toString()
    }

    private fun calculatePass(): Int {
        var pass = 0
        for (i in trainingDays.indices.reversed()) {
            if (trainingDays[i].getProgressPercent() >= 100) pass++ else break
        }
        return pass
    }
}