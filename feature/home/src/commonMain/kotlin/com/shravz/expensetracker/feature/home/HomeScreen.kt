package com.shravz.expensetracker.feature.home

// Import necessary composables, remove ones that are no longer directly used here for the chart card
import androidx.compose.foundation.layout.Arrangement // Keep if used elsewhere
import androidx.compose.foundation.layout.Box // Keep if used elsewhere
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme // Keep
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Keep
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shravz.expensetracker.feature.home.di.HomeModule
import com.shravz.expensetracker.feature.home.presentation.HomeViewModel
import com.shravz.expensetracker.feature.home.presentation.components.ExpenseLineChart // This is now the main chart UI
import com.shravz.expensetracker.feature.home.presentation.components.RecipientsList
import com.shravz.expensetracker.feature.home.presentation.components.TransactionsList
// ExpenseTimeRange might not be needed directly here if it's fully handled within ExpenseLineChart's new params
// import com.shravz.expensetracker.feature.home.presentation.model.ExpenseTimeRange
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun HomeScreen() {
    val viewModel = remember { HomeModule.provideHomeViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.homeData != null) {
            Text(
                text = "Welcome, ${uiState.homeData?.userName}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Call the refactored ExpenseLineChart
            ExpenseLineChart(
                expenses = uiState.chartExpenses,
                selectedTimeRange = uiState.selectedTimeRange,
                onTimeRangeSelected = { range -> viewModel.setExpenseTimeRange(range) },
                isLoading = uiState.isLoading,
                chartTitle = "Expense Trend",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), // Modifier for the Card inside ExpenseLineChart
                onDataPointClick = { expense ->
                    println("Clicked on expense: Date: ${expense.date}, Amount: ${expense.amount}, Category: ${expense.category}")
                    // Handle data point click if needed
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            uiState.homeData?.recipients?.let { recipients ->
                RecipientsList(recipients = recipients)
            }

            Spacer(modifier = Modifier.height(24.dp))

            uiState.homeData?.recentTransactions?.let { transactions ->
                TransactionsList(transactions = transactions)
            }

            Spacer(modifier = Modifier.height(16.dp))

        } else if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { // Ensure progress is centered if it's the only thing
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
        } else if (uiState.error != null) {
            Text(
                text = "Error: ${uiState.error}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen() // This preview might need adjustments if HomeViewModel dependencies are complex
        // or if uiState needs to be mocked for a meaningful preview of ExpenseLineChart.
    }
}