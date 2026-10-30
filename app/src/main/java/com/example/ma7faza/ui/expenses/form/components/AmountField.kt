package com.example.ma7faza.ui.expenses.form.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ma7faza.ui.expenses.form.ExpenseFormValidationError
import com.example.ma7faza.ui.expenses.form.toMessage
import com.example.test.attach_money

@Composable
fun AmountField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    error: ExpenseFormValidationError? = null
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = modifier.border(
            2.dp, MaterialTheme.colorScheme.primary, CardDefaults.shape
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Amount",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            InputField(
                state = state,
                leadingIcon = attach_money,
                placeholder = "0.00",
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                keyboardType = KeyboardType.Decimal
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
}