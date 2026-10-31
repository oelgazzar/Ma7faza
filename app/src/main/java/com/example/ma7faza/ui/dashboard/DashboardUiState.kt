package com.example.ma7faza.ui.dashboard

import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionSummary

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(
        val balanceMinor: Long,
        val summary: TransactionSummary,
        val recentTransactions: List<Transaction>,
        val selectedPeriod: SummaryPeriod,
        val categories: Map<String, Category>
    ) : DashboardUiState
    data class Error(val message: String? = null) : DashboardUiState
}
