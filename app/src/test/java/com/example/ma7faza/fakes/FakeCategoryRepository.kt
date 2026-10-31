package com.example.ma7faza.fakes

import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCategoryRepository : CategoryRepository {
    override fun observeByType(type: TransactionType): Flow<List<Category>> =
        flowOf(
            when (type) {
                TransactionType.EXPENSE -> listOf(
                    Category("OTHER_EXPENSE", "other_expense", type, true, "more", "neutral"),
                    Category("FOOD", "food", type, true, "restaurant", "orange")
                )
                TransactionType.INCOME -> listOf(
                    Category("OTHER_INCOME", "other_income", type, true, "more", "neutral"),
                    Category("SALARY", "salary", type, true, "work", "green")
                )
            }
        )
}
