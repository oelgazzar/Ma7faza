package com.example.ma7faza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.example.ma7faza.data.sample.sampleExpenses
import com.example.ma7faza.domain.repository.ExpenseRepository
import com.example.ma7faza.ui.expenses.form.ExpenseFormScreen
import com.example.ma7faza.ui.navigation.AppNavHost
import com.example.ma7faza.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: ExpenseRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            sampleExpenses.forEach {
                repository.upsert(it)
            }
        }
        enableEdgeToEdge()
        setContent {
            AppTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    AppNavHost()
}

