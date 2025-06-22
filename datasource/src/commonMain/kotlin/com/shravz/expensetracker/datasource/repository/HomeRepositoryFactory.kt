package com.shravz.expensetracker.datasource.repository

import com.shravz.expensetracker.datasource.local.HomeLocalDataSource
import com.shravz.expensetracker.datasource.network.HomeNetworkDataSource

/**
 * Factory interface for creating HomeRepository instances
 */
interface HomeRepositoryFactory {
    /**
     * Create a HomeRepository instance
     */
    fun create(): HomeRepository
}

/**
 * Default implementation of HomeRepositoryFactory
 */
class DefaultHomeRepositoryFactory(
    private val networkDataSource: HomeNetworkDataSource,
    private val localDataSource: HomeLocalDataSource
) : HomeRepositoryFactory {
    override fun create(): HomeRepository {
        return HomeRepository(networkDataSource, localDataSource)
    }
}