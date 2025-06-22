package com.shravz.expensetracker.datasource.repository

import com.shravz.expensetracker.datasource.local.IosHomeLocalDataSourceFactory
import com.shravz.expensetracker.datasource.network.HomeNetworkDataSource
import com.shravz.expensetracker.datasource.network.MockHomeNetworkDataSource

/**
 * iOS-specific implementation of HomeRepositoryFactory
 */
class IosHomeRepositoryFactory : HomeRepositoryFactory {
    
    private val networkDataSource: HomeNetworkDataSource = MockHomeNetworkDataSource()
    private val localDataSourceFactory = IosHomeLocalDataSourceFactory()
    
    override fun create(): HomeRepository {
        val localDataSource = localDataSourceFactory.create()
        return HomeRepository(networkDataSource, localDataSource)
    }
}