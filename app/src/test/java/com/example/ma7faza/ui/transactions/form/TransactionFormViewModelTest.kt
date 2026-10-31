package com.example.ma7faza.ui.transactions.form

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.fakes.FakeCategoryRepository
import com.example.ma7faza.fakes.FakeTransactionRepository
import junit.framework.AssertionFailedError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
class TransactionFormViewModelTest {
    private lateinit var viewModel: TransactionFormViewModel
    private val transactionRepository = FakeTransactionRepository()
    private val categoryRepository = FakeCategoryRepository()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = TransactionFormViewModel(
            transactionRepository,
            categoryRepository,
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun `viewModel with empty state not saved`() = runTest {
        viewModel.save()
        assertEquals(0, transactionRepository.transactions.size)
    }

    @Test
    fun `viewModel with valid state saved`() = runTest {
        val formState = (viewModel.uiState.value as? TransactionFormUiState.Success)?.formState
        formState ?: throw AssertionFailedError("formState is null")
        formState.name.setTextAndPlaceCursorAtEnd("Shopping")
        formState.amount.setTextAndPlaceCursorAtEnd("25")
        viewModel.save()
        assertEquals(1, transactionRepository.transactions.size)
        val transaction = transactionRepository.transactions.first()
        assertEquals("Shopping", transaction.name)
        assertEquals(2500L, transaction.amountMinor)
        assertEquals(TransactionType.EXPENSE, transaction.type)
    }

    @Test
    fun `viewModel saves income transaction with selected type`() = runTest {
        val formState = (viewModel.uiState.value as? TransactionFormUiState.Success)?.formState
        formState ?: throw AssertionFailedError("formState is null")
        formState.name.setTextAndPlaceCursorAtEnd("Salary")
        formState.amount.setTextAndPlaceCursorAtEnd("25000")
        formState.updateType(TransactionType.INCOME)

        viewModel.save()

        val transaction = transactionRepository.transactions.single()
        assertEquals("Salary", transaction.name)
        assertEquals(2_500_000L, transaction.amountMinor)
        assertEquals(TransactionType.INCOME, transaction.type)
    }

    @Test
    fun `viewModel send event when saved`() = runTest {
        viewModel.events.test {
            val formState = (viewModel.uiState.value as? TransactionFormUiState.Success)?.formState
            formState ?: throw AssertionFailedError("formState is null")
            formState.name.setTextAndPlaceCursorAtEnd("Shopping")
            formState.amount.setTextAndPlaceCursorAtEnd("25")
            viewModel.save()
            val event = awaitItem()
            assertEquals(TransactionFormUiEvent.OnSaveDone, event)
            assertEquals(SubmissionState.Idle, viewModel.submissionState.value)
        }
    }

    @Test
    fun `viewModel send nothing when saved with invalid state`() = runTest {
        viewModel.events.test {
            viewModel.save()
            expectNoEvents()
        }
    }

    @Test
    fun `viewModel send error event when repository throw error`() = runTest {
        transactionRepository.shouldThrowOnUpsert = true
        viewModel.events.test {
            val formState = (viewModel.uiState.value as? TransactionFormUiState.Success)?.formState
            formState ?: throw AssertionFailedError("formState is null")
            formState.name.setTextAndPlaceCursorAtEnd("Shopping")
            formState.amount.setTextAndPlaceCursorAtEnd("25")
            viewModel.save()
            assertEquals(TransactionFormUiEvent.OnSaveError, awaitItem())
            assertEquals(SubmissionState.Idle, viewModel.submissionState.value)
            assertEquals("Shopping", formState.name.text.toString())
            assertEquals("25", formState.amount.text.toString())
        }
    }

    @Test
    fun `viewModel with existing transaction loaded`() = runTest {
        transactionRepository.upsert(
            Transaction(
                id = 1L,
                name = "Lunch",
                amountMinor = 1999,
                date = LocalDate.of(2026, 10, 29),
                type = TransactionType.EXPENSE
            )
        )
        val viewModel = TransactionFormViewModel(
            transactionRepository,
            savedStateHandle = SavedStateHandle(mapOf("id" to 1L))
        )
        val formState = (viewModel.uiState.value as? TransactionFormUiState.Success)?.formState
        formState ?: throw AssertionFailedError("formState is null")
        assertEquals(1L, formState.id)
    }

    @Test
    fun `viewModel with nonExisting transaction return error`() = runTest {
        val viewModel = TransactionFormViewModel(
            transactionRepository,
            savedStateHandle = SavedStateHandle(mapOf("id" to 1L))
        )
        assertEquals(TransactionFormUiState.Error, viewModel.uiState.value)
    }

    @Test
    fun `viewModel editing existing transaction loaded updates it`() = runTest {
        transactionRepository.upsert(
            Transaction(
                id = 1L,
                name = "Lunch",
                amountMinor = 1999,
                date = LocalDate.of(2026, 10, 29),
                type = TransactionType.EXPENSE
            )
        )
        val viewModel = TransactionFormViewModel(
            transactionRepository,
            savedStateHandle = SavedStateHandle(mapOf("id" to 1L))
        )
        val formState = (viewModel.uiState.value as? TransactionFormUiState.Success)?.formState
        formState ?: throw AssertionFailedError("formState is null")
        formState.amount.setTextAndPlaceCursorAtEnd("25")
        viewModel.save()
        assertEquals(1, transactionRepository.transactions.size)
        val transaction = transactionRepository.transactions.first()
        assertEquals(2500L, transaction.amountMinor)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewModel save calls upsert once while saving`() = runTest {
        transactionRepository.shouldSuspenOnUpsert = true
        val formState = (viewModel.uiState.value as? TransactionFormUiState.Success)?.formState
        formState ?: throw AssertionFailedError("formState is null")
        formState.name.setTextAndPlaceCursorAtEnd("Shopping")
        formState.amount.setTextAndPlaceCursorAtEnd("25")
        viewModel.save()
        viewModel.save()
        assertEquals(1, transactionRepository.upsertCalls)
        assertEquals(SubmissionState.Saving, viewModel.submissionState.value)

        transactionRepository.upsertGate.complete(Unit)
        advanceUntilIdle()

        assertEquals(SubmissionState.Idle, viewModel.submissionState.value)
    }
}

class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}