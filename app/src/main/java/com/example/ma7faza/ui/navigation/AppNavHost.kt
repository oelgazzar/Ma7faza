package com.example.ma7faza.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ma7faza.ui.transactions.form.TransactionForm
import com.example.ma7faza.ui.transactions.form.TransactionFormScreen
import com.example.ma7faza.ui.transactions.list.TransactionList
import com.example.ma7faza.ui.transactions.list.TransactionListScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        startDestination = TransactionList
    ) {
        composable<TransactionList> {
            TransactionListScreen(
                onAddTransaction = { navController.navigate(TransactionForm()) },
                onEditTransaction = { id -> navController.navigate((TransactionForm(id))) }
            )
        }

        composable<TransactionForm> {
            TransactionFormScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}