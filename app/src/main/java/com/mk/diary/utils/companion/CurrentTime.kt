package com.mk.mydiary.utils.companion

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




object CurrentTime {
    private val sdf = SimpleDateFormat("hh:mm a dd/MMM/yyyy", Locale.getDefault())
    private val time = SimpleDateFormat("hh:mm a", Locale.getDefault())
    private val dateMonth = SimpleDateFormat("dd", Locale.getDefault()) // Modified to only get day
    private val year = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault())

    val currentTime: String
        get() = time.format(Date())

    val currentDate: String
        get() = dateMonth.format(Date())

    val currentMonth: String
        get() {
            val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
            return monthFormat.format(Date())
        }

    val currentYear: String
        get() = year.format(Date())

    val currentDayOfWeek: String
        get() = dayOfWeek.format(Date())
}


