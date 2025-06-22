package com.shravz.expensetracker.datasource.di

import android.content.Context
import com.shravz.expensetracker.datasource.network.MockHomeNetworkDataSource
import com.shravz.expensetracker.datasource.repository.AndroidHomeRepositoryFactory
import com.shravz.expensetracker.datasource.repository.HomeRepository
import com.shravz.expensetracker.datasource.repository.HomeRepositoryFactory

/**
 * Android-specific dependency injection module for the datasource module
 */
object AndroidDataSourceModule : DataSourceModule {
    private var context: Context? = null

    /**
     * Initialize the module with the application context
     */
    fun initialize(applicationContext: Context) {
        context = applicationContext
        // Initialize the DataSourceModuleProvider with this module
        DataSourceModuleProvider.initialize(this)
    }

    /**
     * Provides a HomeRepositoryFactory instance
     */
    override fun provideHomeRepositoryFactory(): HomeRepositoryFactory {
        val appContext = requireNotNull(context) { 
            "AndroidDataSourceModule must be initialized with a Context before use" 
        }
        val networkDataSource = MockHomeNetworkDataSource()
        return AndroidHomeRepositoryFactory(appContext, networkDataSource)
    }

    /**
     * Provides a HomeRepository instance
     */
    override fun provideHomeRepository(): HomeRepository {
        return provideHomeRepositoryFactory().create()
    }
}
