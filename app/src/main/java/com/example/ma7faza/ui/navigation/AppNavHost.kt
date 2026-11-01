package com.example.ma7faza.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ma7faza.ui.dashboard.DashboardScreen
import com.example.ma7faza.ui.transactions.form.TransactionForm
import com.example.ma7faza.ui.transactions.form.TransactionFormScreen
import com.example.ma7faza.ui.transactions.list.TransactionList
import com.example.ma7faza.ui.transactions.list.TransactionListScreen
import kotlinx.serialization.Serializable

@Serializable
data object Dashboard

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Dashboard) {
        composable<Dashboard> {
            DashboardScreen(
                onAddTransaction = { navController.navigate(TransactionForm()) },
                onEditTransaction = { navController.navigate(TransactionForm(it)) },
                onSeeAllTransactions = { navController.navigate(TransactionList) }
            )
        }
        composable<TransactionList> {
            TransactionListScreen(
                onAddTransaction = { navController.navigate(TransactionForm()) },
                onEditTransaction = { navController.navigate(TransactionForm(it)) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<TransactionForm> {
            TransactionFormScreen(navigateBack = { navController.popBackStack() })
        }
    }
}
