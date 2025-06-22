package com.shravz.expensetracker.datasource.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for the application
 */
@Database(
    entities = [
        ExpenseEntity::class,
        RecipientEntity::class,
        TransactionEntity::class,
        UserDataEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun recipientDao(): RecipientDao
    abstract fun transactionDao(): TransactionDao
    abstract fun userDataDao(): UserDataDao

    companion object {
        private const val DATABASE_NAME = "expense_tracker_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}