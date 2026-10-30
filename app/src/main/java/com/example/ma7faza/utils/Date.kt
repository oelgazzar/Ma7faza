package com.example.ma7faza.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyy")

fun LocalDate.toDisplayString(): String = this.format(dateFormatter)