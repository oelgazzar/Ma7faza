package com.example.ma7faza.ui.transactions.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.domain.repository.CategoryRepository
import com.example.ma7faza.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val transactionId = savedStateHandle.toRoute<TransactionForm>().id
    private val selectedType = MutableStateFlow(TransactionType.EXPENSE)
    val categories = selectedType
        .flatMapLatest(categoryRepository::observeByType)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

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
                val transaction = transactionRepository.observe(id).first()
                transaction?.let {
                    selectedType.value = it.type
                    _uiState.value = TransactionFormUiState.Success(TransactionFormState.fromTransaction(it))
                } ?: run {
                    _uiState.value = TransactionFormUiState.Error
                }
            }
        } ?: run {
            _uiState.value = TransactionFormUiState.Success(TransactionFormState())
        }
    }

    fun updateType(formState: TransactionFormState, type: TransactionType) {
        formState.updateType(type)
        selectedType.value = type
    }

    fun updateCategory(formState: TransactionFormState, categoryId: String) {
        formState.updateCategory(categoryId)
    }

    fun save() {
        val form = (uiState.value as? TransactionFormUiState.Success)?.formState ?: return
        if (_submissionState.value == SubmissionState.Saving) return

        form.validate()?.let { transaction ->
            _submissionState.value = SubmissionState.Saving
            viewModelScope.launch {
                try {
                    transactionRepository.upsert(transaction)
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
