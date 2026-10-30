package com.example.ma7faza.domain.models

import java.time.LocalDate

data class Transaction(
    val id: Long = 0,
    val name: String,
    val amountMinor: Long,
    val note: String? = null,
    val date: LocalDate,
    val type: TransactionType
)
