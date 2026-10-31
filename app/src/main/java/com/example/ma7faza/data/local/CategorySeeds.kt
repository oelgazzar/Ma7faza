package com.example.ma7faza.data.local

import com.example.ma7faza.domain.models.TransactionType

object CategorySeeds {
    val all = listOf(
        CategoryEntity("OTHER_EXPENSE", "other_expense", TransactionType.EXPENSE, true, "more", "neutral"),
        CategoryEntity("FOOD", "food", TransactionType.EXPENSE, true, "restaurant", "orange"),
        CategoryEntity("TRANSPORT", "transport", TransactionType.EXPENSE, true, "directions_car", "blue"),
        CategoryEntity("SHOPPING", "shopping", TransactionType.EXPENSE, true, "shopping_bag", "purple"),
        CategoryEntity("BILLS", "bills", TransactionType.EXPENSE, true, "receipt", "red"),
        CategoryEntity("HEALTH", "health", TransactionType.EXPENSE, true, "medical_services", "green"),
        CategoryEntity("ENTERTAINMENT", "entertainment", TransactionType.EXPENSE, true, "movie", "pink"),
        CategoryEntity("EDUCATION", "education", TransactionType.EXPENSE, true, "school", "indigo"),
        CategoryEntity("OTHER_INCOME", "other_income", TransactionType.INCOME, true, "more", "neutral"),
        CategoryEntity("SALARY", "salary", TransactionType.INCOME, true, "work", "green"),
        CategoryEntity("FREELANCE", "freelance", TransactionType.INCOME, true, "computer", "teal"),
        CategoryEntity("INVESTMENT", "investment", TransactionType.INCOME, true, "trending_up", "blue"),
        CategoryEntity("GIFT", "gift", TransactionType.INCOME, true, "card_giftcard", "pink")
    )
}
