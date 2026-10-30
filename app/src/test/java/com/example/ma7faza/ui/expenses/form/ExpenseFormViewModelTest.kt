package com.example.ma7faza.ui.expenses.form

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.fakes.FakeExpenseRepository
import junit.framework.AssertionFailedError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class ExpenseFormViewModelTest {
    private lateinit var viewModel: ExpenseFormViewModel
    private val expenseRepository = FakeExpenseRepository()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = ExpenseFormViewModel(
            expenseRepository,
            savedStateHandle = SavedStateHandle()
        )
    }


    @Test
    fun `viewModel with empty state not saved`() = runTest {
        viewModel.save()
        assertEquals(0, expenseRepository.expenses.size)
    }

    @Test
    fun `viewModel with valid state saved`() = runTest {
        val formState = (viewModel.uiState.value as? ExpenseFormUiState.Success)?.formState
        formState ?: throw AssertionFailedError("formState is null")
        formState.name.setTextAndPlaceCursorAtEnd("Shopping")
        formState.amount.setTextAndPlaceCursorAtEnd("25")
        viewModel.save()
        assertEquals(1, expenseRepository.expenses.size)
        val expense = expenseRepository.expenses.first()
        assertEquals("Shopping", expense.name)
        assertEquals(2500L, expense.amountMinor)
    }

    @Test
    fun `viewModel send event when saved`() = runTest {
        viewModel.events.test {
            val formState = (viewModel.uiState.value as? ExpenseFormUiState.Success)?.formState
            formState ?: throw AssertionFailedError("formState is null")
            formState.name.setTextAndPlaceCursorAtEnd("Shopping")
            formState.amount.setTextAndPlaceCursorAtEnd("25")
            viewModel.save()
            val event = awaitItem()
            assertEquals(ExpenseFormUiEvent.OnSaveDone, event)
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
    fun `viewModel with existing expense loaded`() = runTest {
        expenseRepository.upsert(
            Expense(
                id = 1L,
                name = "Lunch",
                amountMinor = 1999,
                note = null,
                date = LocalDate.of(2026, 10, 29)
            )
        )
        val viewModel = ExpenseFormViewModel(
            expenseRepository,
            savedStateHandle = SavedStateHandle(mapOf("id" to 1L))
        )
        val formState = (viewModel.uiState.value as? ExpenseFormUiState.Success)?.formState
        formState ?: throw AssertionFailedError("formState is null")
        assertEquals(1L, formState.id)
    }

    @Test
    fun `viewModel with nonExisting expense return error`() = runTest {
        val viewModel = ExpenseFormViewModel(
            expenseRepository,
            savedStateHandle = SavedStateHandle(mapOf("id" to 1L))
        )
        assertEquals(ExpenseFormUiState.Error(ExpenseFormError.ExpenseNotFound), viewModel.uiState.value)
    }

    @Test
    fun `viewModel editing existing expense loaded updates it`() = runTest {
        expenseRepository.upsert(
            Expense(
                id = 1L,
                name = "Lunch",
                amountMinor = 1999,
                note = null,
                date = LocalDate.of(2026, 10, 29)
            )
        )
        val viewModel = ExpenseFormViewModel(
            expenseRepository,
            savedStateHandle = SavedStateHandle(mapOf("id" to 1L))
        )
        val formState = (viewModel.uiState.value as? ExpenseFormUiState.Success)?.formState
        formState ?: throw AssertionFailedError("formState is null")
        formState.amount.setTextAndPlaceCursorAtEnd("25")
        viewModel.save()
        assertEquals(1, expenseRepository.expenses.size)
        val expense = expenseRepository.expenses.first()
        assertEquals(2500L, expense.amountMinor)
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
