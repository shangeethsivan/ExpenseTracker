package com.shravz.expensetracker.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shravz.expensetracker.feature.home.model.Recipient

/**
 * A component that displays a list of money recipients as circular avatars.
 */
@Composable
fun RecipientsList(
    recipients: List<Recipient>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Recipients",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(recipients) { recipient ->
                RecipientItem(recipient = recipient)
            }
        }
    }
}

/**
 * A circular avatar for a single recipient.
 */
@Composable
private fun RecipientItem(
    recipient: Recipient,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, // Retain for potential external modifications like specific spacing
        shape = CircleShape, // Make the card itself circular
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // Avatar placeholder (first letter of name)
        Box(
            modifier = Modifier
                .size(56.dp) // Consistent size for all avatars
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = recipient.name.firstOrNull()?.toString() ?: "", // Display first letter, handle empty name
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium // Adjust typography as needed for a single letter
            )
        }
    }
}
