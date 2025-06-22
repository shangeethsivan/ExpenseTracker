package com.shravz.expensetracker.datasource.network.connectivity

/**
 * Factory for creating NetworkConnectivityChecker instances
 */
interface NetworkConnectivityCheckerFactory {
    /**
     * Create a NetworkConnectivityChecker instance
     */
    fun create(): NetworkConnectivityChecker
}

/**
 * Singleton provider for NetworkConnectivityCheckerFactory
 */
object NetworkConnectivityCheckerProvider {
    private var factory: NetworkConnectivityCheckerFactory? = null
    
    /**
     * Initialize the provider with a factory
     */
    fun initialize(factory: NetworkConnectivityCheckerFactory) {
        this.factory = factory
    }
    
    /**
     * Get a NetworkConnectivityChecker instance
     * @throws IllegalStateException if the factory has not been initialized
     */
    fun get(): NetworkConnectivityChecker {
        return factory?.create() ?: throw IllegalStateException(
            "NetworkConnectivityCheckerFactory has not been initialized. " +
            "Call NetworkConnectivityCheckerProvider.initialize() first."
        )
    }
}