package com.example.ma7faza.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ma7faza.ui.expenses.form.ExpenseForm
import com.example.ma7faza.ui.expenses.form.ExpenseFormScreen
import com.example.ma7faza.ui.expenses.list.ExpenseList
import com.example.ma7faza.ui.expenses.list.ExpenseListScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        startDestination = ExpenseForm()
    ) {
        composable<ExpenseList> {
            ExpenseListScreen()
        }

        composable<ExpenseForm> {
            ExpenseFormScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}