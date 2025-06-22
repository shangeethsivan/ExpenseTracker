package com.shravz.expensetracker

import android.app.Application
import com.shravz.expensetracker.datasource.di.AndroidDataSourceModule
import com.shravz.expensetracker.datasource.network.connectivity.AndroidNetworkConnectivityCheckerFactory
import com.shravz.expensetracker.datasource.network.connectivity.NetworkConnectivityCheckerProvider

/**
 * Application class for the Expense Tracker app
 */
class ExpenseTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize the AndroidDataSourceModule with the application context
        AndroidDataSourceModule.initialize(applicationContext)

        // Initialize the NetworkConnectivityCheckerProvider with the AndroidNetworkConnectivityCheckerFactory
        NetworkConnectivityCheckerProvider.initialize(AndroidNetworkConnectivityCheckerFactory(applicationContext))
    }
}
