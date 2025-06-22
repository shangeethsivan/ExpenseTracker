package com.shravz.expensetracker.datasource.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shravz.expensetracker.model.Expense
import com.shravz.expensetracker.model.Recipient
import com.shravz.expensetracker.model.Transaction

/**
 * Room entity for expenses
 */
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val amount: Double,
    val date: String, // Format: "YYYY-MM-DD"
    val category: String
) {
    fun toModel(): Expense = Expense(
        id = id,
        amount = amount,
        date = date,
        category = category
    )

    companion object {
        fun fromModel(expense: Expense): ExpenseEntity = ExpenseEntity(
            id = expense.id,
            amount = expense.amount,
            date = expense.date,
            category = expense.category
        )
    }
}

/**
 * Room entity for recipients
 */
@Entity(tableName = "recipients")
data class RecipientEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val recentAmount: Double
) {
    fun toModel(): Recipient = Recipient(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        recentAmount = recentAmount
    )

    companion object {
        fun fromModel(recipient: Recipient): RecipientEntity = RecipientEntity(
            id = recipient.id,
            name = recipient.name,
            avatarUrl = recipient.avatarUrl,
            recentAmount = recipient.recentAmount
        )
    }
}

/**
 * Room entity for transactions
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val amount: Double,
    val date: String, // Format: "YYYY-MM-DD"
    val recipientId: String,
    val recipientName: String,
    val category: String,
    val isIncome: Boolean
) {
    fun toModel(): Transaction = Transaction(
        id = id,
        amount = amount,
        date = date,
        recipientId = recipientId,
        recipientName = recipientName,
        category = category,
        isIncome = isIncome
    )

    companion object {
        fun fromModel(transaction: Transaction): TransactionEntity = TransactionEntity(
            id = transaction.id,
            amount = transaction.amount,
            date = transaction.date,
            recipientId = transaction.recipientId,
            recipientName = transaction.recipientName,
            category = transaction.category,
            isIncome = transaction.isIncome
        )
    }
}

/**
 * Entity to store user data
 */
@Entity(tableName = "user_data")
data class UserDataEntity(
    @PrimaryKey
    val id: String = "user_data", // Single row for user data
    val userName: String,
    val balance: Double
)