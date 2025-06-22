package com.shravz.expensetracker

import androidx.compose.ui.window.ComposeUIViewController
import com.shravz.expensetracker.datasource.di.IosDataSourceModule
import com.shravz.expensetracker.datasource.network.connectivity.IosNetworkConnectivityCheckerFactory
import com.shravz.expensetracker.datasource.network.connectivity.NetworkConnectivityCheckerProvider

fun MainViewController() = ComposeUIViewController { 
    // Initialize the iOS-specific implementations
    IosDataSourceModule.initialize()
    NetworkConnectivityCheckerProvider.initialize(IosNetworkConnectivityCheckerFactory())

    App() 
}
