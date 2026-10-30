package com.example.ma7faza.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


fun LocalDate.toDisplayString(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.getDefault())
    return this.format(dateFormatter)
}