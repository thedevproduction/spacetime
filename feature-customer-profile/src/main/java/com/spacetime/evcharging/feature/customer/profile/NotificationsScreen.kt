package com.spacetime.evcharging.feature.customer.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun NotificationsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedTab by remember { mutableStateOf("ALL") }

    val filteredList = when (selectedTab) {
        "CHARGING" -> uiState.notifications.filter { it.category == "CHARGING" }
        "PAYMENT" -> uiState.notifications.filter { it.category == "PAYMENT" }
        "OFFER" -> uiState.notifications.filter { it.category == "OFFER" }
        else -> uiState.notifications
    }

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Notifications",
                showBack = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("ALL" to "All", "CHARGING" to "Charging", "PAYMENT" to "Payments", "OFFER" to "Offers").forEach { (key, label) ->
                    val isSelected = selectedTab == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTab = key },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary.copy(alpha = 0.15f),
                            selectedLabelColor = Primary
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) { notif ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, if (!notif.isRead) Primary.copy(alpha = 0.4f) else Border),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = when (notif.category) {
                                    "CHARGING" -> Primary.copy(alpha = 0.12f)
                                    "PAYMENT" -> Success.copy(alpha = 0.12f)
                                    else -> Warning.copy(alpha = 0.12f)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = when (notif.category) {
                                            "CHARGING" -> Icons.Default.Bolt
                                            "PAYMENT" -> Icons.Default.Payment
                                            else -> Icons.Default.LocalOffer
                                        },
                                        contentDescription = null,
                                        tint = when (notif.category) {
                                            "CHARGING" -> Primary
                                            "PAYMENT" -> Success
                                            else -> Warning
                                        },
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MainText)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(notif.message, fontSize = 12.sp, color = SecondaryText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(notif.timestamp, fontSize = 11.sp, color = SecondaryText)
                            }
                        }
                    }
                }
            }
        }
    }
}
