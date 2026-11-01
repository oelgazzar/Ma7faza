package com.example.ma7faza.ui.transactions.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ma7faza.R
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.TransactionType
import java.time.Instant
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TransactionFilterSheet(
    initialFilters: TransactionFilterState,
    categories: List<Category>,
    onApply: (TransactionFilterState) -> Unit,
    modifier: Modifier = Modifier
) {
    var temporaryFilters by remember(initialFilters) { mutableStateOf(initialFilters) }
    var datePickerTarget by remember { mutableStateOf<DatePickerTarget?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.filter_transactions),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.transaction_type),
            style = MaterialTheme.typography.titleMedium
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TransactionTypeFilter.entries.forEach { type ->
                FilterChip(
                    selected = temporaryFilters.type == type,
                    onClick = {
                        val compatibleCategoryIds = categories.filter { category ->
                            type == TransactionTypeFilter.ALL ||
                                    (type == TransactionTypeFilter.INCOME && category.type == TransactionType.INCOME) ||
                                    (type == TransactionTypeFilter.EXPENSE && category.type == TransactionType.EXPENSE)
                        }.map { it.id }.toSet()
                        temporaryFilters = temporaryFilters.copy(
                            type = type,
                            categoryIds = temporaryFilters.categoryIds.intersect(
                                compatibleCategoryIds
                            )
                        )
                    },
                    label = {
                        Text(
                            when (type) {
                                TransactionTypeFilter.ALL -> stringResource(R.string.all_transactions)
                                TransactionTypeFilter.INCOME -> stringResource(R.string.income)
                                TransactionTypeFilter.EXPENSE -> stringResource(R.string.expense)
                            }
                        )
                    }
                )
            }
        }

        HorizontalDivider()
        Text(stringResource(R.string.category), style = MaterialTheme.typography.titleMedium)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FilterChip(
                selected = temporaryFilters.categoryIds.isEmpty(),
                onClick = { temporaryFilters = temporaryFilters.copy(categoryIds = emptySet()) },
                label = { Text(stringResource(R.string.all_transactions)) }
            )
            categories.filter { category ->
                temporaryFilters.type == TransactionTypeFilter.ALL ||
                        (temporaryFilters.type == TransactionTypeFilter.INCOME && category.type == TransactionType.INCOME) ||
                        (temporaryFilters.type == TransactionTypeFilter.EXPENSE && category.type == TransactionType.EXPENSE)
            }.forEach { category ->
                FilterChip(
                    selected = category.id in temporaryFilters.categoryIds,
                    onClick = {
                        val selected = temporaryFilters.categoryIds.toMutableSet()
                        if (!selected.add(category.id)) selected.remove(category.id)
                        temporaryFilters = temporaryFilters.copy(categoryIds = selected)
                    },
                    label = { Text(category.name) }
                )
            }
        }

        HorizontalDivider()
        Text(stringResource(R.string.date_range), style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = temporaryFilters.startDate != null,
                onClick = { datePickerTarget = DatePickerTarget.START },
                label = {
                    Text(
                        temporaryFilters.startDate?.toString() ?: stringResource(R.string.from_date)
                    )
                }
            )
            FilterChip(
                selected = temporaryFilters.endDate != null,
                onClick = { datePickerTarget = DatePickerTarget.END },
                label = {
                    Text(
                        temporaryFilters.endDate?.toString() ?: stringResource(R.string.to_date)
                    )
                }
            )
        }
        if (temporaryFilters.startDate != null || temporaryFilters.endDate != null) {
            TextButton(onClick = {
                temporaryFilters = temporaryFilters.copy(startDate = null, endDate = null)
            }) { Text(stringResource(R.string.clear_date_range)) }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
                    temporaryFilters = TransactionFilterState()
                }
            ) {
                Text(stringResource(R.string.reset))
            }
            Button(onClick = { onApply(temporaryFilters) }) {
                Text(stringResource(R.string.apply))
            }
        }
    }

    datePickerTarget?.let { target ->
        val initialDate = if (target == DatePickerTarget.START) temporaryFilters.startDate else temporaryFilters.endDate
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()
                ?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { datePickerTarget = null },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = pickerState.selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    if (selectedDate != null) {
                        temporaryFilters = if (target == DatePickerTarget.START) {
                            temporaryFilters.copy(
                                startDate = selectedDate,
                                endDate = temporaryFilters.endDate?.takeIf {
                                    !it.isBefore(
                                        selectedDate
                                    )
                                }
                            )
                        } else {
                            temporaryFilters.copy(
                                endDate = selectedDate,
                                startDate = temporaryFilters.startDate?.takeIf {
                                    !it.isAfter(
                                        selectedDate
                                    )
                                }
                            )
                        }
                    }
                    datePickerTarget = null
                }) { Text(stringResource(R.string.apply)) }
            },
            dismissButton = {
                TextButton(onClick = { datePickerTarget = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) { DatePicker(state = pickerState) }
    }
}

private enum class DatePickerTarget { START, END }