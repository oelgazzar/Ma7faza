package com.example.ma7faza.utils

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class MoneyTest {
    @Test
    fun `parseAmountMinor should return correct value for valid input`() {
        val amount = "19.99"
        val result = parseAmount(amount)
        assertEquals(1999L, result)
    }

    @Test
    fun `parseAmountMinor should return null for input with more than 2 decimal places`() {
        val amount = "5.999"
        val result = parseAmount(amount)
        assertNull(result)
    }

    @Test
    fun `parseAmountMinor should return null for empty Input`() {
        val amount = ""
        val result = parseAmount(amount)
        assertNull(result)
    }

    @Test
    fun `parseAmountMinor should return null for negative input`() {
        val amount = "-3"
        val result = parseAmount(amount)
        assertNull(result)
    }

    @Test
    fun `parseAmountMinor should return null for input with comma separator`() {
        val amount = "12,5"
        val result = parseAmount(amount)
        assertNull(result)
    }

    @Test
    fun `parseAmountMinor should return 0 for '0' input`() {
        val amount = "0"
        val result = parseAmount(amount)
        assertEquals(0L, result)
    }

    @Test
    fun `parseAmountMinor should work if input has extra spaces`() {
        val amount = " 12.5 "
        val result = parseAmount(amount)
        assertEquals(1250L, result)
    }
}