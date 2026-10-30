package com.example.ma7faza.ui.transactions.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.ui.transactions.form.components.AmountField
import com.example.ma7faza.ui.transactions.form.components.DateField
import com.example.ma7faza.ui.transactions.form.components.ErrorScreen
import com.example.ma7faza.ui.transactions.form.components.LoadingScreen
import com.example.ma7faza.ui.transactions.form.components.NameField
import com.example.ma7faza.ui.transactions.form.components.NoteField
import com.example.ma7faza.ui.theme.AppTheme
import com.example.ma7faza.ui.transactions.form.components.TransactionTypeSelector
import kotlinx.serialization.Serializable

@Serializable
data class TransactionForm(val id: Long? = null)

@Composable
fun TransactionFormScreen(
    navigateBack: () -> Unit,
    viewModel: TransactionFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val submissionState by viewModel.submissionState.collectAsStateWithLifecycle()
    val snackBarState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when(event) {
                TransactionFormUiEvent.OnSaveDone -> {
                    navigateBack()
                }
                TransactionFormUiEvent.OnSaveError -> {
                        snackBarState.showSnackbar("Failed to save expense")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarState) }
    ) { innerPadding ->
        when (val ui = uiState) {
            TransactionFormUiState.Loading -> {
                LoadingScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            TransactionFormUiState.Error -> {
                ErrorScreen(
                    onBack = navigateBack,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is TransactionFormUiState.Success ->
                TransactionFormContent(
                    formState = ui.formState,
                    submissionState = submissionState,
                    onSave = {
                        keyboardController?.hide()
                        viewModel.save()
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
        }
    }
}

@Composable
fun TransactionFormContent(
    formState: TransactionFormState,
    submissionState: SubmissionState,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        TransactionTypeSelector(
            selectedType = formState.type,
            onTypeSelected = formState::updateType,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        AmountField(
            formState.amount,
            error = formState.errors.firstOrNull { it.field == "amount" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        NameField(
            formState.name,
            error = formState.errors.firstOrNull { it.field == "name" }
        )
        Spacer(modifier = Modifier.height(16.dp))


        DateField(formState.date, onDateSelected = formState::updateDate)
        Spacer(modifier = Modifier.height(16.dp))

        NoteField(formState.note)
        Spacer(modifier = Modifier.height(32.dp))


        SaveButton(
            onClick = onSave,
            isSaving = submissionState == SubmissionState.Saving,
        )
    }
}

@Composable
fun SaveButton(
    onClick: () -> Unit,
    isSaving: Boolean = false
) {
    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        enabled = !isSaving,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (isSaving) {
            CircularProgressIndicator()
        } else {
            Text("Save")
        }
    }
}

@Preview
@Composable
private fun IdleTransactionFormContentPrev() {
    AppTheme {
        TransactionFormContent(
            formState = TransactionFormState(),
            onSave = {},
            submissionState = SubmissionState.Idle,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun ErrorIdleTransactionFormContentPrev() {
    AppTheme {
        TransactionFormContent(
            formState = TransactionFormState().apply { validate() },
            onSave = {},
            submissionState = SubmissionState.Idle,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SavingTransactionFormContentPrev() {
    AppTheme {
        TransactionFormContent(
            formState = TransactionFormState(),
            onSave = {},
            submissionState = SubmissionState.Saving,
            modifier = Modifier.padding(16.dp)
        )
    }
}