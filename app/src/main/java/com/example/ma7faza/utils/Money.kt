package com.example.ma7faza.utils

import java.math.BigDecimal

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