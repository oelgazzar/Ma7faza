package com.example.ma7faza.ui.transactions.form

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.repository.CategoryRepository
import com.example.ma7faza.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val _uiState: MutableStateFlow<TransactionFormUiState> =
        MutableStateFlow(TransactionFormUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState = _submissionState.asStateFlow()

    private val _events = Channel<TransactionFormUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    private val _mode = MutableStateFlow<TransactionFormMode?>(null)
    val mode = _mode.asStateFlow()

    init {
        transactionId?.let { id ->
            viewModelScope.launch {
                val transaction = transactionRepository.observe(id).first()
                transaction?.let {
                    showForm(TransactionFormState.fromTransaction(it))
                } ?: run {
                    _uiState.value = TransactionFormUiState.Error
                }
            }
        } ?: run {
            showForm(TransactionFormState())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun showForm(formState: TransactionFormState) {
        _uiState.value = TransactionFormUiState.Success(formState)
        _mode.value = if (transactionId == null) TransactionFormMode.New else TransactionFormMode.Edit

        viewModelScope.launch {
            snapshotFlow { formState.type }
                .distinctUntilChanged()
                .flatMapLatest(categoryRepository::observeByType)
                .collect(formState::updateCategories)
        }
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

enum class TransactionFormMode {
    New,
    Edit
}
