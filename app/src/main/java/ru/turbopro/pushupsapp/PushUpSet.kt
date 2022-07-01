package ru.turbopro.pushupsapp

class PushUpSet(
    var type: String,
    var startTime: Long
) {
    private var count = 0
    private var endTime: Long = 0

    override fun toString(): String {
        return "Count: $count Start time: $startTime End time: $endTime"
    }

    fun getCount(): Int {
        return count
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun getEndTime(): Long = endTime

    fun setEndTime(endTime: Long) {
        this.endTime = endTime
    }
}