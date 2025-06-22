package com.shravz.expensetracker.feature.home.di

import com.shravz.expensetracker.datasource.di.DataSourceModuleProvider
import com.shravz.expensetracker.datasource.repository.HomeRepository
import com.shravz.expensetracker.feature.home.presentation.HomeViewModel

/**
 * Dependency injection module for the home feature
 */
object HomeModule {

    // Get the repository from the DataSourceModule
    private val repository: HomeRepository by lazy { 
        DataSourceModuleProvider.get().provideHomeRepository()
    }

    /**
     * Provides a HomeViewModel instance
     */
    fun provideHomeViewModel(): HomeViewModel {
        return HomeViewModel(repository)
    }
}
