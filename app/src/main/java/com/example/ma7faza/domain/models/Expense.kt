package com.example.ma7faza.domain.models

import java.time.LocalDate

data class Expense(
    val id: Long = 0,
    val name: String,
    val amountMinor: Long,
    val note: String? = null,
    val date: LocalDate
)
