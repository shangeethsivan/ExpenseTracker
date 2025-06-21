package com.shravz.expensetracker.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shravz.expensetracker.feature.home.di.HomeModule
import com.shravz.expensetracker.feature.home.presentation.components.ExpenseLineChart
import com.shravz.expensetracker.feature.home.presentation.components.RecipientsList
import com.shravz.expensetracker.feature.home.presentation.components.TransactionsList
import expensetracker.feature.home.generated.resources.Res
import expensetracker.feature.home.generated.resources.bell_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.abs // Required for kotlin.math.abs
import kotlin.math.ceil // Required for kotlin.math.ceil
import kotlin.math.floor // Required for kotlin.math.floor


@Composable
fun HomeScreen() {
    val viewModel = remember { HomeModule.provideHomeViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp), // Overall vertical padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val homeData = uiState.homeData
        if (homeData != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Padding for the Row content
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Welcome, ${homeData.userName}!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(resource = Res.drawable.bell_24),
                    contentDescription = "Notifications",
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(0.5.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Balance Display
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(
                    text = formatBalanceWithCommas(homeData.balance), // Use the KMP formatting function
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alignByBaseline()
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Balance",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black.copy(alpha = 0.5f), // Consider MaterialTheme.colorScheme.onSurfaceVariant
                    modifier = Modifier.alignByBaseline()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExpenseLineChart(
                expenses = uiState.chartExpenses,
                selectedTimeRange = uiState.selectedTimeRange,
                onTimeRangeSelected = { range -> viewModel.setExpenseTimeRange(range) },
                isLoading = uiState.isLoading,
                chartTitle = "Expense Trend",
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                onDataPointClick = { expense ->
                    println("Clicked on expense: Date: ${expense.date}, Amount: ${expense.amount}, Category: ${expense.category}")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (homeData.recipients.isNotEmpty()) {
                RecipientsList(
                    recipients = homeData.recipients,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (homeData.recentTransactions.isNotEmpty()) {
                TransactionsList(
                    transactions = homeData.recentTransactions,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

        } else if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
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
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No data available.")
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}

// Helper function to format balance with commas for Kotlin Multiplatform
private fun formatBalanceWithCommas(balance: Double?): String {
    if (balance == null) return "$0.00"

    // Round to two decimal places correctly for positive and negative numbers
    val balanceTimes100 = (balance * 100.0).let {
        // Simple rounding to nearest by adding/subtracting 0.5 before casting/truncating
        // For positive: floor(x + 0.5), For negative: ceil(x - 0.5)
        // A common way is to add 0.5 * sign(balance) then truncate/floor
        // However, toLong() truncates towards zero.
        // Let's use a method that works well for financial rounding (round half up typically)
        // (balance * 100.0).roundToLong() is simpler if available and does round half to even or similar
        // For basic rounding to 2 decimal places:
        val temp = if (balance >= 0) floor(it + 0.5) else ceil(it - 0.5)
        temp.toLong()
    }


    val integerPartValue = balanceTimes100 / 100
    val decimalPartValue = abs(balanceTimes100 % 100)

    val decimalStr = decimalPartValue.toString().padStart(2, '0')

    var integerStr = abs(integerPartValue).toString()
    val isNegative = balance < 0.0

    if (integerStr.length > 3) {
        val reversedInteger = integerStr.reversed()
        val chunked = reversedInteger.chunked(3)
        integerStr = chunked.joinToString(",").reversed()
    }

    val sign = if (isNegative) "-" else ""
    return "$sign$$integerStr.$decimalStr"
}