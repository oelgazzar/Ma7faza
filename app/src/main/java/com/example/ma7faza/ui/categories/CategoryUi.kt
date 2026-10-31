package com.example.ma7faza.ui.categories

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.ma7faza.R
import com.example.ma7faza.domain.models.Category

@Composable
fun Category.displayName(): String = when (id) {
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
fun Category.color(): Color {
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
