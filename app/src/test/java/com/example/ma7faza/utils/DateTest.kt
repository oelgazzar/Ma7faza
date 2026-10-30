package com.example.ma7faza.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateTest {
    @Test
    fun `test date formatting`() {
        val date = LocalDate.of(2026, 1, 20)
        assertEquals("Tue, 20 Jan 2026", date.toDisplayString())
    }
}