package com.example.ma7faza.ui.dashboard

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class SummaryPeriodTest {
    private val today = LocalDate.of(2026, 10, 31)

    @Test fun thisWeekStartsMonday() {
        assertEquals(LocalDate.of(2026, 10, 26) to today, SummaryPeriod.THIS_WEEK.toDateRange(today))
    }

    @Test fun thisMonthStartsOnFirst() {
        assertEquals(LocalDate.of(2026, 10, 1) to today, SummaryPeriod.THIS_MONTH.toDateRange(today))
    }

    @Test fun lastMonthCoversPreviousMonth() {
        assertEquals(LocalDate.of(2026, 9, 1) to LocalDate.of(2026, 9, 30), SummaryPeriod.LAST_MONTH.toDateRange(today))
    }

    @Test fun allTimeHasNoDateRange() {
        assertEquals(null, SummaryPeriod.ALL_TIME.toDateRange(today))
    }
}
