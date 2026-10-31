package com.example.ma7faza.data.local
import androidx.room3.Room
import com.example.ma7faza.domain.models.TransactionType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class CategoryDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: CategoryDao
    @Before fun setUp() {
        database = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.getApplication(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        dao = database.categoryDao()
    }
    @After fun tearDown() { database.close() }
    @Test fun observeByTypeReturnsOnlyMatchingCategories() = runBlocking {
        CategorySeeds.all.forEach { dao.upsert(it) }
        val expenses = dao.observeByType(TransactionType.EXPENSE).first()
        val income = dao.observeByType(TransactionType.INCOME).first()
        assertTrue(expenses.isNotEmpty() && expenses.all { it.type == TransactionType.EXPENSE })
        assertTrue(income.isNotEmpty() && income.all { it.type == TransactionType.INCOME })
    }
}
