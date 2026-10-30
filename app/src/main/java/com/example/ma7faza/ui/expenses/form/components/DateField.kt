package com.example.ma7faza.ui.expenses.form.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DateField(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Text(
        "Date",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(4.dp))
    DatePickerField(
        selectedDate = selectedDate,
        onDateSelected = onDateSelected
    )
}