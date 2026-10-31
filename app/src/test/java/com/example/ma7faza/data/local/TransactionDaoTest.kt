package com.example.ma7faza.data.local

import androidx.room3.Room
import com.example.ma7faza.domain.models.TransactionType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class TransactionDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: TransactionDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.transactionDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `empty database has zero income and expenses`() = runBlocking {
        assertEquals(
            TransactionSummaryResult(incomeMinor = 0L, expensesMinor = 0L),
            dao.observeSummary().first()
        )
    }

    @Test
    fun `summary separates income and expenses`() = runBlocking {
        dao.upsert(transaction(id = 1, amountMinor = 250_000, type = TransactionType.INCOME))
        dao.upsert(transaction(id = 2, amountMinor = 75_000, type = TransactionType.EXPENSE))
        dao.upsert(transaction(id = 3, amountMinor = 25_000, type = TransactionType.EXPENSE))

        assertEquals(
            TransactionSummaryResult(incomeMinor = 250_000L, expensesMinor = 100_000L),
            dao.observeSummary().first()
        )
    }

    @Test
    fun `summary updates after inserting a transaction`() = runBlocking {
        dao.upsert(transaction(id = 1, amountMinor = 100_000, type = TransactionType.INCOME))
        assertEquals(
            TransactionSummaryResult(incomeMinor = 100_000L, expensesMinor = 0L),
            dao.observeSummary().first()
        )

        dao.upsert(transaction(id = 2, amountMinor = 30_000, type = TransactionType.EXPENSE))

        assertEquals(
            TransactionSummaryResult(incomeMinor = 100_000L, expensesMinor = 30_000L),
            dao.observeSummary().first()
        )
    }

    @Test
    fun `summary updates after deleting a transaction`() = runBlocking {
        dao.upsert(transaction(id = 1, amountMinor = 100_000, type = TransactionType.INCOME))
        dao.upsert(transaction(id = 2, amountMinor = 30_000, type = TransactionType.EXPENSE))
        dao.delete(id = 1)

        assertEquals(
            TransactionSummaryResult(incomeMinor = 0L, expensesMinor = 30_000L),
            dao.observeSummary().first()
        )
    }

    private fun transaction(
        id: Long,
        amountMinor: Long,
        type: TransactionType
    ) = TransactionEntity(
        id = id,
        name = "Transaction $id",
        amountMinor = amountMinor,
        date = LocalDate.of(2026, 10, 31),
        type = type
    )
}
