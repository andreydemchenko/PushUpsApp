package ru.turbopro.pushupsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun trainingClick() {
        val intent = Intent(this, TrainingActivity::class.java)
        startActivity(intent)
    }

    fun goalsClick() {
        val intent = Intent(this, GoalsActivity::class.java)
        startActivity(intent)
    }

    fun statisticsClick() {
        val intent = Intent(this, StatisticsActivity::class.java)
        startActivity(intent)
    }
}