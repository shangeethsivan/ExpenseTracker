package com.shravz.expensetracker.feature.home.data.local

import com.shravz.expensetracker.feature.home.model.HomeData
import com.shravz.expensetracker.feature.home.model.Expense
import com.shravz.expensetracker.feature.home.model.Recipient
import com.shravz.expensetracker.feature.home.model.Transaction

/**
 * Interface for local data operations related to home screen
 */
interface HomeLocalDataSource {
    suspend fun getHomeData(): HomeData?
    suspend fun saveHomeData(homeData: HomeData)
    suspend fun getExpenses(): List<Expense>
    suspend fun getRecipients(): List<Recipient>
    suspend fun getTransactions(): List<Transaction>
    suspend fun saveExpenses(expenses: List<Expense>)
    suspend fun saveRecipients(recipients: List<Recipient>)
    suspend fun saveTransactions(transactions: List<Transaction>)
    suspend fun saveUserName(userName: String)
    suspend fun getUserName(): String?
}

/**
 * In-memory implementation of HomeLocalDataSource for testing and development
 * This will be replaced with a Room implementation later
 */
class InMemoryHomeLocalDataSource : HomeLocalDataSource {
    private var cachedHomeData: HomeData? = null

    override suspend fun getHomeData(): HomeData? = cachedHomeData

    override suspend fun saveHomeData(homeData: HomeData) {
        cachedHomeData = homeData
    }

    override suspend fun getExpenses(): List<Expense> = cachedHomeData?.expenses ?: emptyList()

    override suspend fun getRecipients(): List<Recipient> =
        cachedHomeData?.recipients ?: emptyList()

    override suspend fun getTransactions(): List<Transaction> =
        cachedHomeData?.recentTransactions ?: emptyList()

    override suspend fun saveExpenses(expenses: List<Expense>) {
        cachedHomeData = cachedHomeData?.copy(expenses = expenses) ?: HomeData(
            "",
            0.0,
            expenses,
            emptyList(),
            emptyList()
        )
    }

    override suspend fun saveRecipients(recipients: List<Recipient>) {
        cachedHomeData = cachedHomeData?.copy(recipients = recipients) ?: HomeData(
            "",
            0.0,
            emptyList(),
            recipients,
            emptyList()
        )
    }

    override suspend fun saveTransactions(transactions: List<Transaction>) {
        cachedHomeData = cachedHomeData?.copy(recentTransactions = transactions) ?: HomeData(
            "",
            0.0,
            emptyList(),
            emptyList(),
            transactions
        )
    }

    override suspend fun saveUserName(userName: String) {
        cachedHomeData = cachedHomeData?.copy(userName = userName) ?: HomeData(
            userName,
            0.0,
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    override suspend fun getUserName(): String? = cachedHomeData?.userName
}