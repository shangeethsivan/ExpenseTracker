package com.shravz.expensetracker.feature.home.presentation.model

enum class ExpenseTimeRange(val displayName: String) {
    DAY_1("1D"),
    DAYS_5("5D"),
    MONTH_1("1M"),
    MONTH_3("3M"),
    MONTH_6("6M"),
    YEAR_1("1Y"),
    // Potentially keep an "ALL" or "MAX" as well
}