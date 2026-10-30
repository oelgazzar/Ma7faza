package com.example.ma7faza.ui.transactions.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ma7faza.utils.toRelativeDisplayString
import java.time.LocalDate

@Composable
fun TransactionGroupHeader(date: LocalDate) {
    Text(
        date.toRelativeDisplayString(),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}