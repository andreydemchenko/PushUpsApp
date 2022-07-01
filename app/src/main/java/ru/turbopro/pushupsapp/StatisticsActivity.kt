package ru.turbopro.pushupsapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.*

class StatisticsActivity : AppCompatActivity() {
    private lateinit var chart: CombinedChart
    lateinit var lastTrainingDays: ArrayList<TrainingDay>
    private lateinit var trainingDays: ArrayList<TrainingDay>
    private lateinit var dayStatisticsLV: ListView
    private lateinit var trainingDay: TrainingDay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        chart = findViewById(R.id.chart)
        dayStatisticsLV = findViewById(R.id.dayStatisticsLV)
        trainingDay = TrainingDay()
        trainingDays = trainingDay.loadTrainingDays(this)
        setLastTrainingDays()
        setChartData()
        createLastTrainingDaysAdapter()
    }

    fun close() {
        finish()
    }

    private fun setChartData() {
        val progressesPercents: MutableList<BarEntry> = ArrayList<BarEntry>()
        for (i in 0 until lastTrainingDays.size) {
            val fI = i.toFloat()
            val barEntry = BarEntry(fI, lastTrainingDays[i].getProgressPercent())
            progressesPercents.add(barEntry)
        }
        val dates = ArrayList<String>()
        for (day in lastTrainingDays) {
             dates.add(day.getDate())
        }
        val barDataSet = BarDataSet(progressesPercents, "Progress %")
        barDataSet.color = Color.parseColor("#ffffff")
        val barData = BarData()
        barData.addDataSet(barDataSet)
        barData.setDrawValues(false)
        barData.barWidth = 0.5f
        val lineDataList = ArrayList<Entry>()
        for (i in -1..7) {
            lineDataList.add(Entry(i.toFloat(), 100F))
        }
        val lineDataSet = LineDataSet(lineDataList, "100%")
        lineDataSet.color = Color.parseColor("#00ff00")
        val lineData = LineData()
        lineData.addDataSet(lineDataSet)
        lineDataSet.setDrawCircles(false)
        lineData.setDrawValues(false)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(dates)
        xAxis.textColor = Color.parseColor("#ffffff")
        xAxis.setDrawGridLines(false)
        xAxis.labelRotationAngle = -60f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)
        xAxis.axisMinimum = -1f
        xAxis.axisMaximum = 7f
        val leftAxis = chart.axisLeft
        leftAxis.textColor = Color.parseColor("#ffffff")
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawAxisLine(false)
        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
        val description = chart.description
        description.isEnabled = false
        val legend = chart.legend
        legend.isEnabled = false
        val data = CombinedData()
        data.setData(barData)
        data.setData(lineData)
        chart.setDrawGridBackground(false)
        chart.setTouchEnabled(false)
        chart.data = data
        chart.animateY(1000)
    }

    private fun setLastTrainingDays() {
        lastTrainingDays = ArrayList()
        for (i in 0..6) {
            lastTrainingDays.add(trainingDays[trainingDays.size - i - 1])
        }
        lastTrainingDays.reverse()
    }

    private fun createLastTrainingDaysAdapter() {
        val trainingDayAdapter: ArrayAdapter<TrainingDay> =
            object : ArrayAdapter<TrainingDay>(this, 0,
                lastTrainingDays) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    var convertView = convertView
                    val currentTrainingDay =
                        lastTrainingDays[lastTrainingDays.size - position - 1]
                    if (convertView == null) {
                        convertView =
                            layoutInflater.inflate(R.layout.day_statistics_layout, null, false)
                    }
                    val dateTV = convertView!!.findViewById<TextView>(R.id.dateTV)
                    val progressPercentTV =
                        convertView.findViewById<TextView>(R.id.progressPercentTV)
                    val progressRatioTV = convertView.findViewById<TextView>(R.id.progressRatioTV)
                    dateTV.text = currentTrainingDay.getDate()
                    progressPercentTV.text =
                        currentTrainingDay.getProgressPercent().toString() + "%"
                    progressRatioTV.text =
                        currentTrainingDay.getProgress().toString()+ " " + getString(R.string.fromTheTarget) + " " + currentTrainingDay.getGoal()
                    return convertView
                }
            }
        dayStatisticsLV.adapter = trainingDayAdapter
    }
}