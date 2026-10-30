package com.example.ma7faza.ui.expenses.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
): ViewModel() {
    val uiState: StateFlow<ExpenseListUiState> = expenseRepository.observeAll()
        .map<List<Expense>, ExpenseListUiState> {
            ExpenseListUiState.Success(it)
        }
        .catch {
            emit(ExpenseListUiState.Error(it.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExpenseListUiState.Loading
        )

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            expenseRepository.delete(id)
        }
    }
}