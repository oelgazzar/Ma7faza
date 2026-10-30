package com.example.ma7faza.ui.expenses.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.ma7faza.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class ExpenseForm(val id: Long? = null)

@HiltViewModel
class ExpenseFormViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val expenseId = savedStateHandle.toRoute<ExpenseForm>().id
    private val _uiState: MutableStateFlow<ExpenseFormUiState> = MutableStateFlow(ExpenseFormUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<ExpenseFormUiEvent>()
    val events = _events.receiveAsFlow()

    init {
        expenseId?.let { id ->
            viewModelScope.launch {
                val expense = expenseRepository.observe(id).first()
                expense?.let {
                    _uiState.value = ExpenseFormUiState.Success(ExpenseFormState.fromExpense(it))
                }?: run {
                    _uiState.value = ExpenseFormUiState.Error(ExpenseFormError.ExpenseNotFound)
                }
            }
        }?:run {
            _uiState.value = ExpenseFormUiState.Success(ExpenseFormState())
        }
    }

    fun save() {
        val form = (uiState.value as? ExpenseFormUiState.Success)?.formState ?: return

        form.validate()?.let { expense ->
            viewModelScope.launch {
                expenseRepository.upsert(expense)
                _events.trySend(ExpenseFormUiEvent.OnSaveDone)
            }
        }
    }
}

sealed interface ExpenseFormUiEvent {
    data object OnSaveDone : ExpenseFormUiEvent
}