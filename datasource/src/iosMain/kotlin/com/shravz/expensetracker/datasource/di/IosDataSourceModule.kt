package com.shravz.expensetracker.datasource.di

import com.shravz.expensetracker.datasource.repository.HomeRepository
import com.shravz.expensetracker.datasource.repository.HomeRepositoryFactory
import com.shravz.expensetracker.datasource.repository.IosHomeRepositoryFactory

/**
 * iOS-specific dependency injection module for the datasource module
 */
object IosDataSourceModule : DataSourceModule {
    
    private val repositoryFactory: HomeRepositoryFactory by lazy { IosHomeRepositoryFactory() }
    
    /**
     * Initialize the module
     */
    fun initialize() {
        // Initialize the DataSourceModuleProvider with this module
        DataSourceModuleProvider.initialize(this)
    }
    
    /**
     * Provides a HomeRepositoryFactory instance
     */
    override fun provideHomeRepositoryFactory(): HomeRepositoryFactory {
        return repositoryFactory
    }
    
    /**
     * Provides a HomeRepository instance
     */
    override fun provideHomeRepository(): HomeRepository {
        return provideHomeRepositoryFactory().create()
    }
}