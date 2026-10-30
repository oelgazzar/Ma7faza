package com.example.ma7faza.ui.expenses.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
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

    var deletedExpense: Expense? = null

    private val _events = Channel<ExpenseListUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun deleteExpense(expense: Expense) {
        println("Delete expense")
        viewModelScope.launch {
            expenseRepository.delete(expense.id)
            println(deletedExpense)
            deletedExpense = expense
            println(deletedExpense)
            _events.trySend(ExpenseListUiEvent.OnDelete)
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            deletedExpense?.let { expenseRepository.upsert(it) }
            deletedExpense = null
        }
    }

    fun onConfirmDelete() {
        deletedExpense = null
    }
}

sealed interface ExpenseListUiEvent {
    data object OnDelete: ExpenseListUiEvent
}