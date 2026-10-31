package com.example.ma7faza.data.local
import com.example.ma7faza.domain.models.TransactionType
object CategorySeeds {
    val all = listOf(
        CategoryEntity("OTHER_EXPENSE", "other_expense", TransactionType.EXPENSE, true, "more"),
        CategoryEntity("FOOD", "food", TransactionType.EXPENSE, true, "restaurant"),
        CategoryEntity("TRANSPORT", "transport", TransactionType.EXPENSE, true, "directions_car"),
        CategoryEntity("SHOPPING", "shopping", TransactionType.EXPENSE, true, "shopping_bag"),
        CategoryEntity("BILLS", "bills", TransactionType.EXPENSE, true, "receipt"),
        CategoryEntity("HEALTH", "health", TransactionType.EXPENSE, true, "medical_services"),
        CategoryEntity("ENTERTAINMENT", "entertainment", TransactionType.EXPENSE, true, "movie"),
        CategoryEntity("EDUCATION", "education", TransactionType.EXPENSE, true, "school"),
        CategoryEntity("OTHER_INCOME", "other_income", TransactionType.INCOME, true, "more"),
        CategoryEntity("SALARY", "salary", TransactionType.INCOME, true, "work"),
        CategoryEntity("FREELANCE", "freelance", TransactionType.INCOME, true, "computer"),
        CategoryEntity("INVESTMENT", "investment", TransactionType.INCOME, true, "trending_up"),
        CategoryEntity("GIFT", "gift", TransactionType.INCOME, true, "card_giftcard")
    )
}
