package com.app.desafiodourado.core.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateUtils {
    private const val PATTERN_COMPLETE_HOUR = "%02d:%02d:%02d"
    private const val PATTERN_COMPLETE_DATA = "dd/MM/yyyy"
    const val INTERVAL = 1000L
    const val EMPTY_HOUR = "00:00:00"

    fun getHours(millisUntilFinished: Long): Triple<Long, Long, Long> {
        val secondsRemaining = millisUntilFinished / 1000
        val hours = secondsRemaining / 3600
        val minutes = (secondsRemaining % 3600) / 60
        val seconds = secondsRemaining % 60
        return Triple(hours, minutes, seconds)
    }

    fun formatHour(hour: Long, minutes: Long, seconds: Long): String {
        return String.format(PATTERN_COMPLETE_HOUR, hour, minutes, seconds)
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat(PATTERN_COMPLETE_DATA)
        return format.format(calendar.time)
    }

    fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun compareDates(date1: String, date2: String): Boolean {
        val format = SimpleDateFormat(PATTERN_COMPLETE_DATA)
        val calendar1 = Calendar.getInstance()
        calendar1.time = format.parse(date1) as Date
        val calendar2 = Calendar.getInstance()
        calendar2.time = format.parse(date2) as Date
        return calendar1.before(calendar2)
    }
}