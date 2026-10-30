package com.example.ma7faza.ui.transactions.form.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ma7faza.ui.transactions.form.TransactionFormValidationError
import com.example.ma7faza.ui.transactions.form.toMessage
import com.example.ma7faza.ui.theme.icons.sell

@Composable
fun NameField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    error: TransactionFormValidationError? = null,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            "Title",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        InputField(
            state = state,
            leadingIcon = sell,
            placeholder = "e.g. Launch at Cafe"
        )
        Spacer(modifier = Modifier.height(4.dp))
        error?.let {
            Text(
                text = error.toMessage(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}