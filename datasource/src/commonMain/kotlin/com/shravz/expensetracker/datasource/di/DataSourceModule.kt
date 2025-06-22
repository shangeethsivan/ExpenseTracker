package com.shravz.expensetracker.datasource.di

import com.shravz.expensetracker.datasource.local.HomeLocalDataSource
import com.shravz.expensetracker.datasource.local.InMemoryHomeLocalDataSource
import com.shravz.expensetracker.datasource.network.HomeNetworkDataSource
import com.shravz.expensetracker.datasource.network.MockHomeNetworkDataSource
import com.shravz.expensetracker.datasource.repository.DefaultHomeRepositoryFactory
import com.shravz.expensetracker.datasource.repository.HomeRepository
import com.shravz.expensetracker.datasource.repository.HomeRepositoryFactory

/**
 * Common dependency injection module for the datasource module
 */
interface DataSourceModule {
    /**
     * Provides a HomeRepositoryFactory instance
     */
    fun provideHomeRepositoryFactory(): HomeRepositoryFactory

    /**
     * Provides a HomeRepository instance
     */
    fun provideHomeRepository(): HomeRepository
}

/**
 * Default implementation of DataSourceModule
 */
object DefaultDataSourceModule : DataSourceModule {
    // Singleton instances
    private val networkDataSource: HomeNetworkDataSource by lazy { MockHomeNetworkDataSource() }
    private val localDataSource: HomeLocalDataSource by lazy { InMemoryHomeLocalDataSource() }
    private val repositoryFactory: HomeRepositoryFactory by lazy { 
        DefaultHomeRepositoryFactory(networkDataSource, localDataSource) 
    }

    override fun provideHomeRepositoryFactory(): HomeRepositoryFactory {
        return repositoryFactory
    }

    override fun provideHomeRepository(): HomeRepository {
        return provideHomeRepositoryFactory().create()
    }
}

/**
 * Singleton DataSourceModule that can be initialized with the appropriate implementation at runtime
 */
object DataSourceModuleProvider {
    private var module: DataSourceModule = DefaultDataSourceModule

    /**
     * Initialize the module with the appropriate implementation
     */
    fun initialize(module: DataSourceModule) {
        this.module = module
    }

    /**
     * Get the current module implementation
     */
    fun get(): DataSourceModule = module
}