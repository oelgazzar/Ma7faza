package com.example.ma7faza.domain.models

data class TransactionSummary(
    val incomeMinor: Long,
    val expensesMinor: Long
) {
    val balanceMinor: Long
        get() = incomeMinor - expensesMinor
}
