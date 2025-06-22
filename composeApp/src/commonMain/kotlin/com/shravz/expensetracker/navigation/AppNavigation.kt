package com.shravz.expensetracker.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shravz.expensetracker.feature.home.HomeScreen
import com.shravz.expensetracker.feature.profile.ProfileScreen
import com.shravz.expensetracker.feature.stats.StatsScreen
import expensetracker.composeapp.generated.resources.Res
import expensetracker.composeapp.generated.resources.outline_add_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppNavigation() {
    var currentDestination by remember { mutableStateOf<Destination?>(Destination.HomeDestination) }

    Scaffold(
        modifier = Modifier.padding(12.dp),
        bottomBar = {
            CustomBottomNavigation(
                items = Destination.bottomNavItems,
                currentDestination = currentDestination,
                onDestinationSelected = { destination ->
                    currentDestination = destination
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (currentDestination) {
                Destination.HomeDestination -> HomeScreen()
                Destination.Stats -> StatsScreen()
                Destination.Profile -> ProfileScreen()
                null -> HomeScreen() // Default to HomeScreen if destination is null
            }
        }
    }
}

@Composable
fun CustomBottomNavigation(
    items: List<Destination>,
    currentDestination: Destination?,
    onDestinationSelected: (Destination) -> Unit
) {
    Row(modifier = Modifier.padding(start = 16.dp)) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .clip(CircleShape)
                .height(64.dp),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*
                // In Android for some reason in runtime the first destination is null always
                // Comment L98-103 this when running on IOS.
                */
                val destination = Destination.HomeDestination
                CustomBottomNavigationItem(
                    destination = destination,
                    isSelected = currentDestination == destination,
                    onClick = { onDestinationSelected(destination) }
                )
                items.filterNotNull().forEach { destination ->
                    val isSelected = currentDestination == destination
                    CustomBottomNavigationItem(
                        destination = destination,
                        isSelected = isSelected,
                        onClick = { onDestinationSelected(destination) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1F))
        FloatingActionButton(
            shape = RoundedCornerShape(50.dp),
            onClick = {
                // TODO: Handle FAB click, e.g., navigate to an "add transaction" screen
                println("FAB Clicked!")
            },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.outline_add_24),
                contentDescription = "Add Transaction",
            )
        }
    }
}

@Composable
fun CustomBottomNavigationItem(
    destination: Destination?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    if (destination == null) return

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(resource = destination.icon),
                contentDescription = "Add Transaction",
                colorFilter = tint(if(isSelected){
                    Color.Black
                }else{
                    Color.White
                })
            )
        if (isSelected) {
            Text(
                text = destination.title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
