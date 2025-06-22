# Expense Tracker

A Compose Multiplatform project targeting Android and iOS platforms, designed to help users track their expenses and manage their finances.

https://github.com/user-attachments/assets/ddc29646-e06d-4a95-a0ef-26178a3c1c39

## Project Structure

The project follows a modular architecture with the following modules:

* `/composeApp` - Main application module containing the app entry points for both Android and iOS platforms, as well as shared UI components and navigation.

* `/feature` - Contains feature modules, each representing a specific functionality of the app:
  - `/feature/home` - Home screen with expense overview, recent transactions, and recipients
  - `/feature/stats` - Statistics and analytics for expenses
  - `/feature/profile` - User profile management

* `/datasource` - Contains data source implementations for both local and network data:
  - Local data storage (Room DB on Android)
  - Network data fetching
  - Repository pattern implementation

* `/model` - Contains shared data models used across the application

## Packaging Structure

Each module follows a consistent packaging structure:

* `commonMain` - Contains code shared across all platforms
* `androidMain` - Contains Android-specific implementations
* `iosMain` - Contains iOS-specific implementations

Within feature modules, the code is further organized into:
* `presentation` - UI components, ViewModels
* `di` - Dependency injection

## Data Source Switching

The app implements an offline-first approach with automatic switching between local database and network data sources:

1. **Repository Pattern**: The `HomeRepository` coordinates between local and network data sources.

2. **Data Flow**:
   - First tries to get data from local database (Room DB on Android)
   - If local data exists, it's immediately displayed to the user
   - Then checks network connectivity
   - If network is available, fetches fresh data from network and updates local database
   - If network is unavailable, continues using local data

3. **Network Connectivity**: Platform-specific implementations check for network availability:
   - Android: Uses `ConnectivityManager` to check for WiFi, cellular, or Ethernet connectivity
   - iOS: Uses platform-specific network connectivity checking

4. **Data Sources**:
   - Local: `HomeLocalDataSource` interface with platform-specific implementations (`RoomHomeLocalDataSource` on Android)
   - Network: `HomeNetworkDataSource` interface with mock implementation for development

This architecture ensures the app works offline while providing up-to-date data when online.

## Learn More

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)