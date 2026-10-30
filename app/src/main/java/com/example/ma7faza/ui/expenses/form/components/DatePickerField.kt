package com.example.ma7faza.ui.expenses.form.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import com.example.test.arrow_forward_ios
import com.example.test.calendar_today
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(selectedDate)

    InputField(
        leadingIcon = calendar_today,
        placeholder = "Select date",
        trailingIcon = arrow_forward_ios,
        readOnly = true,
        modifier = Modifier
            .textFieldClickable(onClick = { showDatePickerDialog = true })

    )

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePickerDialog = false
                    datePickerState.getSelectedDate()?.let {
                        onDateSelected(it)
                    }
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false}) {
                    Text("Cancel")
                }
            },
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

fun Modifier.textFieldClickable(onClick: () -> Unit): Modifier {
    return this.pointerInput(Unit) {
        awaitEachGesture {
                val down = awaitFirstDown(pass = PointerEventPass.Initial).apply { consume() }
                val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                if (up != null) {
                    up.consume()
                    onClick()
                }
        }
    }
}