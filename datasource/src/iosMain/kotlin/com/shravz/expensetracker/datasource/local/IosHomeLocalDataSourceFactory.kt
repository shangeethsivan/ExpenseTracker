package com.shravz.expensetracker.datasource.local

import com.shravz.expensetracker.model.HomeData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Factory for creating HomeLocalDataSource instances for iOS
 */
class IosHomeLocalDataSourceFactory {
    
    /**
     * Create a HomeLocalDataSource instance for iOS
     * For simplicity, we're using the InMemoryHomeLocalDataSource
     */
    fun create(): HomeLocalDataSource {
        val localDataSource = InMemoryHomeLocalDataSource()
        
        // Initialize with some fake data
        CoroutineScope(Dispatchers.Default).launch {
            if (localDataSource.getHomeData() == null) {
                try {
                    val fakeData = generateFakeHomeData()
                    localDataSource.saveHomeData(fakeData)
                } catch (e: Exception) {
                    // Log the error but continue
                    println("Error initializing local data source with fake data: ${e.message}")
                }
            }
        }
        
        return localDataSource
    }
    
    /**
     * Generate fake home data for testing
     */
    private fun generateFakeHomeData(): HomeData {
        // For simplicity, we're returning a simple HomeData object
        // In a real app, you would generate more realistic data
        return HomeData(
            userName = "John (iOS)",
            balance = 25000.0,
            expenses = emptyList(),
            recipients = emptyList(),
            recentTransactions = emptyList()
        )
    }
}