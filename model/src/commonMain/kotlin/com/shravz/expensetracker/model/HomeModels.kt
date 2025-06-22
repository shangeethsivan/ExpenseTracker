package com.shravz.expensetracker.model

/**
 * Represents an expense entry with amount and date
 */
data class Expense(
    val id: String,
    val amount: Double,
    val date: String, // Format: "YYYY-MM-DD"
    val category: String
)

/**
 * Represents a recipient of money
 */
data class Recipient(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val recentAmount: Double
)

/**
 * Represents a transaction in the history
 */
data class Transaction(
    val id: String,
    val amount: Double,
    val date: String, // Format: "YYYY-MM-DD"
    val recipientId: String,
    val recipientName: String,
    val category: String,
    val isIncome: Boolean
)

/**
 * Represents the complete data needed for the home screen
 */
data class HomeData(
    val userName: String,
    val balance: Double,
    val expenses: List<Expense>,
    val recipients: List<Recipient>,
    val recentTransactions: List<Transaction>
)