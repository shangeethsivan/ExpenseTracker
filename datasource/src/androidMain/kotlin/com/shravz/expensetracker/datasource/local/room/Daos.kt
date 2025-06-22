package com.shravz.expensetracker.datasource.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

/**
 * DAO for expenses
 */
@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<ExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseEntity>)

    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()

    @Transaction
    suspend fun replaceAllExpenses(expenses: List<ExpenseEntity>) {
        deleteAllExpenses()
        insertExpenses(expenses)
    }
}

/**
 * DAO for recipients
 */
@Dao
interface RecipientDao {
    @Query("SELECT * FROM recipients")
    suspend fun getAllRecipients(): List<RecipientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipients(recipients: List<RecipientEntity>)

    @Query("DELETE FROM recipients")
    suspend fun deleteAllRecipients()

    @Transaction
    suspend fun replaceAllRecipients(recipients: List<RecipientEntity>) {
        deleteAllRecipients()
        insertRecipients(recipients)
    }
}

/**
 * DAO for transactions
 */
@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    @Transaction
    suspend fun replaceAllTransactions(transactions: List<TransactionEntity>) {
        deleteAllTransactions()
        insertTransactions(transactions)
    }
}

/**
 * DAO for user data
 */
@Dao
interface UserDataDao {
    @Query("SELECT * FROM user_data LIMIT 1")
    suspend fun getUserData(): UserDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userData: UserDataEntity)

    @Query("DELETE FROM user_data")
    suspend fun deleteUserData()

    @Transaction
    suspend fun updateUserData(userData: UserDataEntity) {
        deleteUserData()
        insertUserData(userData)
    }
}