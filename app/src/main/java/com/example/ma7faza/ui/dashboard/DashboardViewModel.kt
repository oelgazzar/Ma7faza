package com.example.ma7faza.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.domain.repository.CategoryRepository
import com.example.ma7faza.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TransactionRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    private val selectedPeriod = MutableStateFlow(SummaryPeriod.THIS_MONTH)

    private val categories = combine(
        categoryRepository.observeByType(TransactionType.EXPENSE),
        categoryRepository.observeByType(TransactionType.INCOME)
    ) { expenses, incomes ->
        (expenses + incomes).associateBy { it.id }
    }

    val uiState = selectedPeriod.flatMapLatest { period ->
        val range = period.toDateRange(LocalDate.now())
        combine(
            repository.observeBalance(),
            repository.observeSummary(range?.first, range?.second),
            repository.observeCategoryExpenses(range?.first, range?.second),
            repository.observeAll().map { it.take(5) },
            categories
        ) { balance, summary, categoryExpenses, recent, categories ->
            DashboardUiState.Success(
                balanceMinor = balance,
                summary = summary,
                categoryExpenses = categoryExpenses,
                recentTransactions = recent,
                selectedPeriod = period,
                categories = categories
            ) as DashboardUiState
        }
    }.catch {
        emit(DashboardUiState.Error(it.message))
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        DashboardUiState.Loading
    )

    fun selectPeriod(period: SummaryPeriod) {
        selectedPeriod.value = period
    }
}
