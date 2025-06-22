package com.shravz.expensetracker.datasource.network.connectivity

import android.content.Context

/**
 * Android-specific implementation of NetworkConnectivityCheckerFactory
 */
class AndroidNetworkConnectivityCheckerFactory(private val context: Context) : NetworkConnectivityCheckerFactory {
    override fun create(): NetworkConnectivityChecker {
        return AndroidNetworkConnectivityChecker(context)
    }
}