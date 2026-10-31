package com.example.ma7faza.ui.transactions.form.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ma7faza.R
import com.example.ma7faza.domain.models.Category

@Composable
fun CategorySelector(
    categories: List<Category>,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (categories.isEmpty()) return

    androidx.compose.foundation.layout.Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.category),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = category.id == selectedCategoryId,
                    onClick = { onCategorySelected(category.id) },
                    label = {
                        Text(category.displayName())
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(category.color(), CircleShape)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun Category.displayName(): String = when (id) {
    "OTHER_EXPENSE" -> stringResource(R.string.other_expense)
    "FOOD" -> stringResource(R.string.food)
    "TRANSPORT" -> stringResource(R.string.transport)
    "SHOPPING" -> stringResource(R.string.shopping)
    "BILLS" -> stringResource(R.string.bills)
    "HEALTH" -> stringResource(R.string.health)
    "ENTERTAINMENT" -> stringResource(R.string.entertainment)
    "EDUCATION" -> stringResource(R.string.education)
    "OTHER_INCOME" -> stringResource(R.string.other_income)
    "SALARY" -> stringResource(R.string.salary)
    "FREELANCE" -> stringResource(R.string.freelance)
    "INVESTMENT" -> stringResource(R.string.investment)
    "GIFT" -> stringResource(R.string.gift)
    else -> name
}

@Composable
private fun Category.color(): Color {
    val colors = MaterialTheme.colorScheme
    return when (colorKey) {
        "orange", "pink", "teal" -> colors.tertiary
        "blue", "indigo" -> colors.primary
        "purple" -> colors.secondary
        "red" -> colors.error
        "green" -> colors.primary
        else -> colors.onSurfaceVariant
    }
}
