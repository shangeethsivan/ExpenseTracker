package com.shravz.expensetracker.datasource.local

import com.shravz.expensetracker.model.Expense
import com.shravz.expensetracker.model.HomeData
import com.shravz.expensetracker.model.Recipient
import com.shravz.expensetracker.model.Transaction
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.datetime.minus

/**
 * Utility class to generate fake data for the Room database
 */
object FakeDataGenerator {
    
    /**
     * Generate fake home data for the Room database
     * This data is slightly different from the network data to make it clear when data is coming from the database
     */
    fun generateFakeHomeData(): HomeData {
        return HomeData(
            userName = "John",
            balance = 25000.0,
            expenses = createFakeExpenses(),
            recipients = createFakeRecipients(),
            recentTransactions = createFakeTransactions()
        )
    }
    
    private fun createFakeExpenses(): List<Expense> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return listOf(
            Expense("local_1", 100.0, today.minus(DatePeriod(days = 25)).toString(), "Food"),
            Expense("local_2", 65.0, today.minus(DatePeriod(days = 20)).toString(), "Transport"),
            Expense("local_3", 180.0, today.minus(DatePeriod(days = 15)).toString(), "Shopping"),
            Expense("local_4", 45.0, today.minus(DatePeriod(days = 10)).toString(), "Entertainment"),
            Expense("local_5", 130.0, today.minus(DatePeriod(days = 5)).toString(), "Utilities"),
            Expense("local_6", 80.0, today.minus(DatePeriod(days = 1)).toString(), "Food")
        )
    }
    
    private fun createFakeRecipients(): List<Recipient> {
        return listOf(
            Recipient("local_1", "Alice (Local)", null, 100.0),
            Recipient("local_2", "Bob (Local)", null, 65.0),
            Recipient("local_3", "Charlie (Local)", null, 180.0),
            Recipient("local_4", "David (Local)", null, 45.0),
            Recipient("local_5", "Esther (Local)", null, 45.0),
            Recipient("local_6", "Frank (Local)", null, 45.0),
            Recipient("local_7", "Gabe (Local)", null, 45.0),
        )
    }
    
    private fun createFakeTransactions(): List<Transaction> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        return listOf(
            Transaction("local_1", 100.0, today.minus(DatePeriod(days = 1)).toString(), "local_1", "Alice (Local)", "Food", false),
            Transaction("local_2", 65.0, today.minus(DatePeriod(days = 5)).toString(), "local_2", "Company (Local)", "Income", true),
            Transaction("local_3", 180.0, today.minus(DatePeriod(days = 10)).toString(), "local_3", "Charlie (Local)", "Shopping", false),
            Transaction("local_4", 45.0, today.minus(DatePeriod(days = 15)).toString(), "local_4", "David (Local)", "Entertainment", false),
            Transaction("local_5", 250.0, today.minus(DatePeriod(days = 20)).toString(), "local_1", "Company (Local)", "Income", true)
        )
    }
}