package com.shravz.expensetracker.datasource.network.connectivity

/**
 * Interface for checking network connectivity
 */
interface NetworkConnectivityChecker {
    /**
     * Check if the device is connected to the internet
     * @return true if connected, false otherwise
     */
    fun isNetworkAvailable(): Boolean
}