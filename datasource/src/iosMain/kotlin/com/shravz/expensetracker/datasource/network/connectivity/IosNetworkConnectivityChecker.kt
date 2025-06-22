package com.shravz.expensetracker.datasource.network.connectivity

import platform.Foundation.NSURLSessionConfiguration
import platform.Foundation.NSURLSession
import platform.Foundation.NSURL
import platform.Foundation.NSURLSessionDataTask
import platform.Foundation.NSData
import platform.Foundation.NSError

/**
 * iOS-specific implementation of NetworkConnectivityChecker
 */
class IosNetworkConnectivityChecker : NetworkConnectivityChecker {
    
    override fun isNetworkAvailable(): Boolean {
        // On iOS, we can check network connectivity by attempting to create a URL session
        // This is a simple implementation that assumes network is available if we can create a session
        return try {
            val configuration = NSURLSessionConfiguration.defaultSessionConfiguration
            val session = NSURLSession.sessionWithConfiguration(configuration)
            true
        } catch (e: Exception) {
            false
        }
    }
}

/**
 * iOS-specific implementation of NetworkConnectivityCheckerFactory
 */
class IosNetworkConnectivityCheckerFactory : NetworkConnectivityCheckerFactory {
    override fun create(): NetworkConnectivityChecker {
        return IosNetworkConnectivityChecker()
    }
}