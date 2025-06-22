package com.shravz.expensetracker.datasource.repository

import com.shravz.expensetracker.datasource.local.HomeLocalDataSource
import com.shravz.expensetracker.datasource.network.HomeNetworkDataSource
import com.shravz.expensetracker.datasource.network.connectivity.NetworkConnectivityCheckerProvider
import com.shravz.expensetracker.model.HomeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Enum to represent the source of data
 */
enum class DataSource {
    LOCAL,
    NETWORK
}

/**
 * Repository for home screen data that coordinates between network and local data sources
 */
class HomeRepository(
    private val networkDataSource: HomeNetworkDataSource,
    private val localDataSource: HomeLocalDataSource
) {
    /**
     * Result class that includes both the data and its source
     */
    data class HomeDataResult(
        val data: HomeData,
        val source: DataSource
    )
    /**
     * Get home data with offline-first approach
     * 1. Try to get data from local database
     * 2. If forceLocalOnly is true, only use local data and don't fetch from network
     * 3. Check if network is available
     * 4. If network is available, fetch fresh data from network and save locally
     * 5. If network is not available or fetch fails, use local data if available
     * 6. If no local data and no network, return error
     * 
     * @param forceLocalOnly If true, only load from local database and skip network fetch
     */
    fun getHomeData(forceLocalOnly: Boolean = false): Flow<Result<HomeDataResult>> = flow {
        // Try to get data from local database
        val localData = localDataSource.getHomeData()

        if (localData != null) {
            // If local data is available, emit it with LOCAL source
            emit(Result.success(HomeDataResult(localData, DataSource.LOCAL)))

            // If we're forcing local only (first launch), return early
            if (forceLocalOnly) {
                return@flow
            }
        }

        // Check if network is available
        val isNetworkAvailable = try {
            NetworkConnectivityCheckerProvider.get().isNetworkAvailable()
        } catch (e: Exception) {
            // If there's an error checking network connectivity, assume it's not available
            false
        }

        // If network is not available, don't attempt to fetch from network
        if (!isNetworkAvailable) {
            // Network is not available
            if (localData == null) {
                // If no local data and no network, emit error
                emit(Result.failure(Exception("No network connection and no local data available")))
            }
            // If we have local data, we've already emitted it, so no need to emit error
            return@flow
        }

        try {
            // Fetch fresh data from network
            val networkResult = networkDataSource.getHomeData()

            if (networkResult.isSuccess) {
                // If network fetch is successful, save to local database and emit with NETWORK source
                val homeData = networkResult.getOrThrow()
                val updatedHomeData = homeData.copy(userName = "John DB", balance = homeData.balance + 20.0)
                localDataSource.saveHomeData(updatedHomeData)
                emit(Result.success(HomeDataResult(homeData, DataSource.NETWORK)))
            } else {
                // If network fetch fails and we don't have local data, emit error
                if (localData == null) {
                    emit(Result.failure(networkResult.exceptionOrNull() ?: Exception("Unknown error")))
                }
                // If we have local data, we've already emitted it, so no need to emit error
            }
        } catch (e: Exception) {
            // If exception occurs and we don't have local data, emit error
            if (localData == null) {
                emit(Result.failure(e))
            }
            // If we have local data, we've already emitted it, so no need to emit error
        }
    }
}
