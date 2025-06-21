package com.shravz.expensetracker.feature.home.data.repository

import com.shravz.expensetracker.feature.home.data.local.HomeLocalDataSource
import com.shravz.expensetracker.feature.home.data.network.HomeNetworkDataSource
import com.shravz.expensetracker.feature.home.model.HomeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository for home screen data that coordinates between network and local data sources
 */
class HomeRepository(
    private val networkDataSource: HomeNetworkDataSource,
    private val localDataSource: HomeLocalDataSource
) {
    /**
     * Get home data with offline-first approach
     * 1. Try to get data from local database
     * 2. If local data is not available, fetch from network and save locally
     * 3. If network fetch fails, return error
     */
    fun getHomeData(): Flow<Result<HomeData>> = flow {
        // Try to get data from local database
        val localData = localDataSource.getHomeData()
        
        if (localData != null) {
            // If local data is available, emit it
            emit(Result.success(localData))
        }
        
        try {
            // Fetch fresh data from network
            val networkResult = networkDataSource.getHomeData()
            
            if (networkResult.isSuccess) {
                // If network fetch is successful, save to local database and emit
                val homeData = networkResult.getOrThrow()
                localDataSource.saveHomeData(homeData)
                emit(Result.success(homeData))
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