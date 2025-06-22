package com.shravz.expensetracker.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shravz.expensetracker.model.Recipient
import kotlin.math.floor

/**
 * A component that displays a list of money recipients as circular avatars.
 * The number of visible avatars is dynamically calculated based on available width.
 * If the number of recipients exceeds this, a summary avatar (+X) is shown.
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

        if (recipients.isNotEmpty()) {
            val avatarSize = 56.dp
            val avatarSpacing = 12.dp
            val horizontalContentPadding = 16.dp // Padding inside the LazyRow for its content

            BoxWithConstraints(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp) // Add some bottom padding to the container if needed
            ) {
                val availableWidth = maxWidth - (horizontalContentPadding * 2) // Subtract LazyRow's internal padding

                val maxVisibleAvatars = remember(availableWidth, avatarSize, avatarSpacing) {
                    if (availableWidth <= 0.dp) {
                        0
                    } else {
                        // Calculate how many items of (avatarSize + avatarSpacing) can fit.
                        // The last item doesn't need trailing spacing for this calculation.
                        floor((availableWidth + avatarSpacing) / (avatarSize + avatarSpacing)).toInt()
                    }
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(avatarSpacing),
                    contentPadding = PaddingValues(horizontal = horizontalContentPadding)
                ) {
                    if (recipients.size > maxVisibleAvatars && maxVisibleAvatars > 0) {
                        // Show (maxVisibleAvatars - 1) actual recipients if there's at least one slot
                        // and we need to show a summary item.
                        val itemsToDisplayDirectly = recipients.take((maxVisibleAvatars - 1).coerceAtLeast(0))
                        items(itemsToDisplayDirectly) { recipient ->
                            RecipientItem(recipient = recipient, size = avatarSize)
                        }
                        // Show summary item for the rest
                        val remainingCount = recipients.size - itemsToDisplayDirectly.size
                        if (remainingCount > 0) {
                            item {
                                SummaryRecipientItem(count = remainingCount, size = avatarSize)
                            }
                        }
                    } else if (maxVisibleAvatars == 0 && recipients.isNotEmpty()) {
                        // Special case: Not enough space for even one avatar, show a single summary for all
                        item {
                            SummaryRecipientItem(count = recipients.size, size = avatarSize)
                        }
                    } else {
                        // Show all recipients if they fit or if maxVisibleAvatars is 0 (and no recipients)
                        items(recipients.take(maxVisibleAvatars.coerceAtLeast(0))) { recipient ->
                            RecipientItem(recipient = recipient, size = avatarSize)
                        }
                    }
                }
            }
        }
        // Optionally, display a message if the list is empty (and not loading)
        // else {
        // Text("No recipients to display.", modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp))
        // }
    }
}

/**
 * A circular avatar for a single recipient, showing the first letter of their name.
 */
@Composable
private fun RecipientItem(
    recipient: Recipient,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = recipient.name.firstOrNull()?.toString()?.uppercase() ?: "",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

/**
 * A circular avatar for summarizing overflow recipients, showing "+X".
 */
@Composable
private fun SummaryRecipientItem(
    count: Int,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(MaterialTheme.colorScheme.secondary), // Use a distinct background
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+$count",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
