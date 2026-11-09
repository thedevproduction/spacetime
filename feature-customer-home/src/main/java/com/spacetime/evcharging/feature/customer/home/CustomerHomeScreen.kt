package com.spacetime.evcharging.feature.customer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun CustomerHomeScreen(
    onNavigateToQr: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToStation: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: CustomerHomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Spacetime Energy",
                onNotificationClick = {},
                notificationCount = 2,
                onProfileClick = onNavigateToProfile
            )
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Finding nearby charging spots...")
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting
            item {
                Column {
                    Text(
                        text = "Hello, ${uiState.userName} 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                    Text(
                        text = "Where would you like to charge today?",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }
            }

            // Wallet card
            item {
                WalletBalanceCard(
                    balance = uiState.walletBalance,
                    onAddMoney = onNavigateToWallet
                )
            }

            // Search bar
            item {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    modifier = Modifier.clickable { onNavigateToExplore() }
                )
            }

            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    QuickActionButton(
                        icon = Icons.Default.QrCodeScanner,
                        label = "Scan QR",
                        onClick = onNavigateToQr
                    )
                    QuickActionButton(
                        icon = Icons.Default.NearMe,
                        label = "Nearby",
                        onClick = onNavigateToExplore
                    )
                    QuickActionButton(
                        icon = Icons.Default.ElectricBolt,
                        label = "Fast Charging",
                        onClick = onNavigateToExplore
                    )
                    QuickActionButton(
                        icon = Icons.Default.Bookmark,
                        label = "Saved",
                        onClick = onNavigateToExplore
                    )
                }
            }

            // Active Charging Banner if present
            uiState.activeSession?.let { session ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.1f)),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Primary)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Success)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Active Charging Session",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${session.stationName} • ${session.energyKwh} kWh",
                                    fontSize = 13.sp,
                                    color = MainText
                                )
                            }
                            Text(
                                text = "View >",
                                fontWeight = FontWeight.Bold,
                                color = Primary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Recommended Stations Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nearby Charging Hubs",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier.clickable { onNavigateToExplore() }
                    )
                }
            }

            // Stations
            items(uiState.nearbyStations) { station ->
                StationCard(
                    name = station.name,
                    address = station.address,
                    distance = "${station.distanceKm} km",
                    availableConnectors = station.availableConnectors,
                    totalConnectors = station.totalConnectors,
                    priceRange = "${station.minimumTariff.displayAmount}/kWh",
                    rating = station.rating,
                    onClick = { onNavigateToStation(station.id) }
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Surface(
            shape = CircleShape,
            color = CardBackground,
            shadowElevation = 2.dp,
            modifier = Modifier.size(54.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MainText
        )
    }
}
