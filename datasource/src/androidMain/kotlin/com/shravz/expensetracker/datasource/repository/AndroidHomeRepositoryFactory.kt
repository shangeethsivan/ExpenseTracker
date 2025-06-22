package com.shravz.expensetracker.datasource.repository

import android.content.Context
import com.shravz.expensetracker.datasource.local.FakeDataGenerator
import com.shravz.expensetracker.datasource.local.RoomHomeLocalDataSource
import com.shravz.expensetracker.datasource.network.HomeNetworkDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Android-specific implementation of HomeRepositoryFactory that uses Room database
 */
class AndroidHomeRepositoryFactory(
    private val context: Context,
    private val networkDataSource: HomeNetworkDataSource
) : HomeRepositoryFactory {
    
    override fun create(): HomeRepository {
        val localDataSource = RoomHomeLocalDataSource(context)
        val repository = HomeRepository(networkDataSource, localDataSource)
        
        // Initialize the database with fake data if it's empty
        // This is done in a background coroutine to avoid blocking the main thread
        CoroutineScope(Dispatchers.IO).launch {
            if (localDataSource.getHomeData() == null) {
                try {
                    val fakeData = FakeDataGenerator.generateFakeHomeData()
                    localDataSource.saveHomeData(fakeData)
                } catch (e: Exception) {
                    // Log the error but continue
                    println("Error initializing database with fake data: ${e.message}")
                }
            }
        }
        
        return repository
    }
}