package com.example.ma7faza.ui.transactions.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.ma7faza.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val transactionId = savedStateHandle.toRoute<TransactionForm>().id
    private val _uiState: MutableStateFlow<TransactionFormUiState> =
        MutableStateFlow(TransactionFormUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState = _submissionState.asStateFlow()

    private val _events = Channel<TransactionFormUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        transactionId?.let { id ->
            viewModelScope.launch {
                val expense = transactionRepository.observe(id).first()
                expense?.let {
                    _uiState.value = TransactionFormUiState.Success(TransactionFormState.fromTransaction(it))
                } ?: run {
                    _uiState.value = TransactionFormUiState.Error
                }
            }
        } ?: run {
            _uiState.value = TransactionFormUiState.Success(TransactionFormState())
        }
    }

    fun save() {
        val form = (uiState.value as? TransactionFormUiState.Success)?.formState ?: return
        if (_submissionState.value == SubmissionState.Saving) return

        form.validate()?.let { expense ->
            _submissionState.value = SubmissionState.Saving
            viewModelScope.launch {
                try {
                    transactionRepository.upsert(expense)
                    _events.trySend(TransactionFormUiEvent.OnSaveDone)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    _events.trySend(TransactionFormUiEvent.OnSaveError)
                } finally {
                    _submissionState.value = SubmissionState.Idle
                }
            }
        }
    }
}

sealed interface TransactionFormUiEvent {
    data object OnSaveDone : TransactionFormUiEvent
    data object OnSaveError : TransactionFormUiEvent
}