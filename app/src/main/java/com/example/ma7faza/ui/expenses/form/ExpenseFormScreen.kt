package com.example.ma7faza.ui.expenses.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ma7faza.ui.expenses.form.components.AmountField
import com.example.ma7faza.ui.expenses.form.components.DateField
import com.example.ma7faza.ui.expenses.form.components.NameField
import com.example.ma7faza.ui.expenses.form.components.NoteField
import com.example.ma7faza.ui.theme.AppTheme

@Composable
fun ExpenseFormScreen(
    viewModel: ExpenseFormViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val submissionState by viewModel.submissionState.collectAsStateWithLifecycle()

    when (val ui = uiState) {
        ExpenseFormUiState.Loading -> Unit
        ExpenseFormUiState.Error -> Unit
        is ExpenseFormUiState.Success ->
            ExpenseFormContent(
                formState = ui.formState,
                submissionState = submissionState,
                onSave = viewModel::save,
                modifier = Modifier.padding(16.dp)
            )
    }
}

@Composable
fun ExpenseFormContent(
    formState: ExpenseFormState,
    submissionState: SubmissionState,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AmountField(formState.amount)
        Spacer(modifier = Modifier.height(16.dp))

        NameField(formState.name)
        Spacer(modifier = Modifier.height(16.dp))

        DateField(formState.date, onDateSelected = formState::updateDate)
        Spacer(modifier = Modifier.height(16.dp))

        NoteField(formState.note)
        Spacer(modifier = Modifier.height(32.dp))

        SaveButton(onClick = onSave, isSaving = submissionState == SubmissionState.Saving)
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
private fun IdleExpenseFormContentPrev() {
    AppTheme {
        ExpenseFormContent(
            formState = ExpenseFormState(),
            onSave = {},
            submissionState = SubmissionState.Idle,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SavingExpenseFormContentPrev() {
    AppTheme {
        ExpenseFormContent(
            formState = ExpenseFormState(),
            onSave = {},
            submissionState = SubmissionState.Saving,
            modifier = Modifier.padding(16.dp)
        )
    }
}