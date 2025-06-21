package com.shravz.expensetracker.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shravz.expensetracker.feature.home.data.repository.HomeRepository
import com.shravz.expensetracker.feature.home.model.Expense
import com.shravz.expensetracker.feature.home.model.HomeData
import com.shravz.expensetracker.feature.home.presentation.model.ExpenseTimeRange // <-- CHANGED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock // <-- ADDED
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod // <-- ADDED
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone // <-- ADDED
import kotlinx.datetime.minus // <-- ADDED
import kotlinx.datetime.todayIn // <-- ADDED

/**
 * ViewModel for the HomeScreen that manages the UI state
 */
class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    data class HomeUiState(
        val isLoading: Boolean = false,
        val homeData: HomeData? = null,
        val chartExpenses: List<Expense> = emptyList(),
        val selectedTimeRange: ExpenseTimeRange = ExpenseTimeRange.MONTH_1, // <-- CHANGED
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedTimeRange = MutableStateFlow(ExpenseTimeRange.MONTH_1) // <-- CHANGED

    private val _rawHomeData = MutableStateFlow<HomeData?>(null)

    init {
        loadHomeData()

        viewModelScope.launch {
            combine(_rawHomeData, _selectedTimeRange) { homeData, timeRange -> // <-- CHANGED (interval to timeRange)
                val processedExpenses = if (homeData != null && homeData.expenses != null) {
                    processExpensesForChart(homeData.expenses, timeRange) // <-- CHANGED
                } else {
                    emptyList()
                }
                _uiState.value.copy(
                    homeData = homeData ?: _uiState.value.homeData,
                    chartExpenses = processedExpenses,
                    selectedTimeRange = timeRange, // <-- CHANGED
                    isLoading = _uiState.value.isLoading
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = _uiState.value
            ).collect { newStatePart ->
                _uiState.update { currentState ->
                    currentState.copy(
                        homeData = newStatePart.homeData,
                        chartExpenses = newStatePart.chartExpenses,
                        selectedTimeRange = newStatePart.selectedTimeRange, // <-- CHANGED
                        isLoading = newStatePart.isLoading
                    )
                 }
            }
        }
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getHomeData().collect { result ->
                result.fold(
                    onSuccess = { homeData ->
                        _rawHomeData.value = homeData
                        _uiState.update {
                            it.copy(isLoading = false, error = null)
                        }
                    },
                    onFailure = { throwable ->
                        _rawHomeData.value = null
                        _uiState.update {
                            it.copy(isLoading = false, error = throwable.message ?: "Unknown error")
                        }
                    }
                )
            }
        }
    }

    fun setExpenseTimeRange(range: ExpenseTimeRange) { // <-- RENAMED and CHANGED parameter type
        _selectedTimeRange.value = range
    }

    private fun processExpensesForChart(expenses: List<Expense>, range: ExpenseTimeRange): List<Expense> {
        if (expenses.isEmpty()) return emptyList()

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val sortedExpenses = expenses.sortedBy { LocalDate.parse(it.date) }

        val filteredExpenses = when (range) {
            ExpenseTimeRange.DAY_1 -> {
                val startDate = today.minus(DatePeriod(days = 1))
                sortedExpenses.filter { LocalDate.parse(it.date) >= startDate && LocalDate.parse(it.date) <= today }
            }
            ExpenseTimeRange.DAYS_5 -> {
                val startDate = today.minus(DatePeriod(days = 5))
                sortedExpenses.filter { LocalDate.parse(it.date) >= startDate && LocalDate.parse(it.date) <= today }
            }
            ExpenseTimeRange.MONTH_1 -> {
                val startDate = today.minus(DatePeriod(months = 1))
                sortedExpenses.filter { LocalDate.parse(it.date) >= startDate && LocalDate.parse(it.date) <= today }
            }
            ExpenseTimeRange.MONTH_3 -> {
                val startDate = today.minus(DatePeriod(months = 3))
                sortedExpenses.filter { LocalDate.parse(it.date) >= startDate && LocalDate.parse(it.date) <= today }
            }
            ExpenseTimeRange.MONTH_6 -> {
                val startDate = today.minus(DatePeriod(months = 6))
                sortedExpenses.filter { LocalDate.parse(it.date) >= startDate && LocalDate.parse(it.date) <= today }
            }
            ExpenseTimeRange.YEAR_1 -> {
                val startDate = today.minus(DatePeriod(years = 1))
                sortedExpenses.filter { LocalDate.parse(it.date) >= startDate && LocalDate.parse(it.date) <= today }
            }
        }
        
        return filteredExpenses.sortedBy { LocalDate.parse(it.date) }
    }
}