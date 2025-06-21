package com.shravz.expensetracker.feature.home.di

import com.shravz.expensetracker.feature.home.data.local.HomeLocalDataSource
import com.shravz.expensetracker.feature.home.data.local.InMemoryHomeLocalDataSource
import com.shravz.expensetracker.feature.home.data.network.HomeNetworkDataSource
import com.shravz.expensetracker.feature.home.data.network.MockHomeNetworkDataSource
import com.shravz.expensetracker.feature.home.data.repository.HomeRepository
import com.shravz.expensetracker.feature.home.presentation.HomeViewModel

/**
 * Dependency injection module for the home feature
 */
object HomeModule {
    
    // Singleton instances
    private val networkDataSource: HomeNetworkDataSource by lazy { MockHomeNetworkDataSource() }
    private val localDataSource: HomeLocalDataSource by lazy { InMemoryHomeLocalDataSource() }
    private val repository: HomeRepository by lazy { 
        HomeRepository(networkDataSource, localDataSource) 
    }
    
    /**
     * Provides a HomeViewModel instance
     */
    fun provideHomeViewModel(): HomeViewModel {
        return HomeViewModel(repository)
    }
}