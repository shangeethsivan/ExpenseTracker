package com.shravz.expensetracker.feature.home.data.network

import com.shravz.expensetracker.feature.home.model.HomeData
import com.shravz.expensetracker.feature.home.model.Expense
import com.shravz.expensetracker.feature.home.model.Recipient
import com.shravz.expensetracker.feature.home.model.Transaction
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod // Ensure this is imported
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus // This is for the operator, DatePeriod constructor is used directly

/**
 * Interface for fetching home data from the network
 */
interface HomeNetworkDataSource {
    suspend fun getHomeData(): Result<HomeData>
}

/**
 * Mock implementation of HomeNetworkDataSource that returns fake data
 */
class MockHomeNetworkDataSource : HomeNetworkDataSource {
    override suspend fun getHomeData(): Result<HomeData> {
        // Simulate network delay
        kotlinx.coroutines.delay(500)
        
        return Result.success(
            HomeData(
                userName = "John",
                balance = 34996.0,
                expenses = createMockExpenses(),
                recipients = createMockRecipients(),
                recentTransactions = createMockTransactions()
            )
        )
    }
    
    private fun createMockExpenses(): List<Expense> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return listOf(
            Expense("1", 120.0, today.minus(DatePeriod(days = 25)).toString(), "Food"),
            Expense("2", 75.0, today.minus(DatePeriod(days = 20)).toString(), "Transport"),
            Expense("3", 200.0, today.minus(DatePeriod(days = 15)).toString(), "Shopping"),
            Expense("4", 50.0, today.minus(DatePeriod(days = 10)).toString(), "Entertainment"),
            Expense("5", 150.0, today.minus(DatePeriod(days = 5)).toString(), "Utilities"),
            Expense("6", 90.0, today.minus(DatePeriod(days = 1)).toString(), "Food")
        )
    }
    
    private fun createMockRecipients(): List<Recipient> {
        return listOf(
            Recipient("1", "Alice", null, 120.0),
            Recipient("2", "Bob", null, 75.0),
            Recipient("3", "Charlie", null, 200.0),
            Recipient("4", "David", null, 50.0),
            Recipient("5", "Esther", null, 50.0),
            Recipient("6", "Frank", null, 50.0),
            Recipient("7", "Gabe", null, 50.0),
        )
    }
    
    private fun createMockTransactions(): List<Transaction> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return listOf(
            Transaction("1", 120.0, today.minus(DatePeriod(days = 1)).toString(), "1", "Alice", "Food", false),
            Transaction("2", 75.0, today.minus(DatePeriod(days = 5)).toString(), "2", "Company", "Income", true),
            Transaction("3", 200.0, today.minus(DatePeriod(days = 10)).toString(), "3", "Charlie", "Shopping", false),
            Transaction("4", 50.0, today.minus(DatePeriod(days = 15)).toString(), "4", "David", "Entertainment", false),
            Transaction("5", 300.0, today.minus(DatePeriod(days = 20)).toString(), "1", "Company", "Income", true)
        )
    }
}