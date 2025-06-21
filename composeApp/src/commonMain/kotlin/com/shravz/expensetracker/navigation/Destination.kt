package com.shravz.expensetracker.navigation

import expensetracker.composeapp.generated.resources.Res
import expensetracker.composeapp.generated.resources.icon_home_24
import expensetracker.composeapp.generated.resources.outline_bar_chart_24
import expensetracker.composeapp.generated.resources.person_24
import org.jetbrains.compose.resources.DrawableResource

sealed class Destination(
    val route: String,
    val title: String,
    val icon: DrawableResource,
) {
    object HomeDestination : Destination(
        route = "home",
        title = "Home",
        icon = Res.drawable.icon_home_24
    )

    object Stats : Destination(
        route = "stats",
        title = "Stats",
        icon = Res.drawable.outline_bar_chart_24,
    )

    object Profile : Destination(
        route = "profile",
        title = "Profile",
        icon = Res.drawable.person_24,
    )

    companion object {
        val bottomNavItems = listOf(HomeDestination, Stats, Profile)
    }
}
