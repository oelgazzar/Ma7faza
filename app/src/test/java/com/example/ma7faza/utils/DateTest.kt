package com.example.ma7faza.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateTest {
    private val today = LocalDate.of(2026, 10, 30)

    @Test
    fun `test date formatting`() {
        assertEquals("Fri, 30 Oct 2026", today.toDisplayString())
    }


    @Test
    fun `today returns Today`() {
        val result = today.toRelativeDisplayString(today)

        assertEquals("Today", result)
    }

    @Test
    fun `yesterday returns Yesterday`() {
        val date = today.minusDays(1)

        val result = date.toRelativeDisplayString(today)

        assertEquals("Yesterday", result)
    }

    @Test
    fun `older date returns formatted date`() {
        val date = today.minusDays(2)

        val result = date.toRelativeDisplayString(today)

        assertEquals("Wed, 28 Oct 2026", result)
    }

    @Test
    fun `future date returns formatted date`() {
        val date = today.plusDays(1)

        val result = date.toRelativeDisplayString(today)

        assertEquals("Tomorrow", result)
    }
}