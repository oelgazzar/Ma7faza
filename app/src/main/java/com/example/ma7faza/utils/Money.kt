package com.example.ma7faza.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun parseAmount(amount: String): Long? {
    return runCatching {
        amount.trim().toBigDecimal()
            .movePointRight(2)
            .longValueExact()
    }.getOrNull()?.takeIf { it >= 0 }
}

fun formatAmount(amount: Long): String {
    return BigDecimal.valueOf(amount, 2).toPlainString()
}

fun formatCurrency(amountMinor: Long): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }

    return "${numberFormat.format(BigDecimal.valueOf(amountMinor, 2))} EGP"
}