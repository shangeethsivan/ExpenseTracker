package com.shravz.expensetracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform