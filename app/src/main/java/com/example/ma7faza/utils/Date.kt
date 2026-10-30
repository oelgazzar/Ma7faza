package com.example.ma7faza.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


fun LocalDate.toDisplayString(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.getDefault())
    return this.format(dateFormatter)
}

fun LocalDate.toRelativeDisplayString(today: LocalDate = LocalDate.now()): String {
    val interval = ChronoUnit.DAYS.between(today, this).toInt()

    return when(interval) {
        -1 -> "Yesterday"
        0 -> "Today"
        1 -> "Tomorrow"
        else -> toDisplayString()
    }
}