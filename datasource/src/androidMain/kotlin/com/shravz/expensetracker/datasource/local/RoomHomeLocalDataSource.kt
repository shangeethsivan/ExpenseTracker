package com.shravz.expensetracker.datasource.local

import android.content.Context
import com.shravz.expensetracker.datasource.local.room.AppDatabase
import com.shravz.expensetracker.datasource.local.room.ExpenseEntity
import com.shravz.expensetracker.datasource.local.room.RecipientEntity
import com.shravz.expensetracker.datasource.local.room.TransactionEntity
import com.shravz.expensetracker.datasource.local.room.UserDataEntity
import com.shravz.expensetracker.model.Expense
import com.shravz.expensetracker.model.HomeData
import com.shravz.expensetracker.model.Recipient
import com.shravz.expensetracker.model.Transaction

/**
 * Room implementation of HomeLocalDataSource
 */
class RoomHomeLocalDataSource(context: Context) : HomeLocalDataSource {
    private val database = AppDatabase.getInstance(context)
    private val expenseDao = database.expenseDao()
    private val recipientDao = database.recipientDao()
    private val transactionDao = database.transactionDao()
    private val userDataDao = database.userDataDao()

    override suspend fun getHomeData(): HomeData? {
        val userData = userDataDao.getUserData() ?: return null
        val expenses = expenseDao.getAllExpenses().map { it.toModel() }
        val recipients = recipientDao.getAllRecipients().map { it.toModel() }
        val transactions = transactionDao.getAllTransactions().map { it.toModel() }

        return HomeData(
            userName = userData.userName,
            balance = userData.balance,
            expenses = expenses,
            recipients = recipients,
            recentTransactions = transactions
        )
    }

    override suspend fun saveHomeData(homeData: HomeData) {
        // Save user data
        userDataDao.updateUserData(
            UserDataEntity(
                userName = homeData.userName,
                balance = homeData.balance
            )
        )

        // Save expenses
        val expenseEntities = homeData.expenses.map { ExpenseEntity.fromModel(it) }
        expenseDao.replaceAllExpenses(expenseEntities)

        // Save recipients
        val recipientEntities = homeData.recipients.map { RecipientEntity.fromModel(it) }
        recipientDao.replaceAllRecipients(recipientEntities)

        // Save transactions
        val transactionEntities = homeData.recentTransactions.map { TransactionEntity.fromModel(it) }
        transactionDao.replaceAllTransactions(transactionEntities)
    }

    override suspend fun getExpenses(): List<Expense> {
        return expenseDao.getAllExpenses().map { it.toModel() }
    }

    override suspend fun getRecipients(): List<Recipient> {
        return recipientDao.getAllRecipients().map { it.toModel() }
    }

    override suspend fun getTransactions(): List<Transaction> {
        return transactionDao.getAllTransactions().map { it.toModel() }
    }

    override suspend fun saveExpenses(expenses: List<Expense>) {
        val expenseEntities = expenses.map { ExpenseEntity.fromModel(it) }
        expenseDao.replaceAllExpenses(expenseEntities)
    }

    override suspend fun saveRecipients(recipients: List<Recipient>) {
        val recipientEntities = recipients.map { RecipientEntity.fromModel(it) }
        recipientDao.replaceAllRecipients(recipientEntities)
    }

    override suspend fun saveTransactions(transactions: List<Transaction>) {
        val transactionEntities = transactions.map { TransactionEntity.fromModel(it) }
        transactionDao.replaceAllTransactions(transactionEntities)
    }

    override suspend fun saveUserName(userName: String) {
        val userData = userDataDao.getUserData()
        userDataDao.updateUserData(
            UserDataEntity(
                userName = userName,
                balance = userData?.balance ?: 0.0
            )
        )
    }

    override suspend fun getUserName(): String? {
        return userDataDao.getUserData()?.userName
    }
}