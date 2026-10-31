package com.example.ma7faza.ui.dashboard

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

enum class SummaryPeriod { THIS_WEEK, THIS_MONTH, LAST_MONTH, ALL_TIME }

fun SummaryPeriod.toDateRange(today: LocalDate = LocalDate.now()): Pair<LocalDate, LocalDate>? =
    when (this) {
        SummaryPeriod.THIS_WEEK -> today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) to today
        SummaryPeriod.THIS_MONTH -> today.withDayOfMonth(1) to today
        SummaryPeriod.LAST_MONTH -> {
            val previous = today.minusMonths(1)
            previous.withDayOfMonth(1) to previous.withDayOfMonth(previous.lengthOfMonth())
        }
        SummaryPeriod.ALL_TIME -> null
    }
