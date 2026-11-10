package com.spacetime.evcharging.feature.customer.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    onNavigateToSavedStations: () -> Unit = onNavigateToExplore,
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToActiveSession: (String) -> Unit = {},
    viewModel: CustomerHomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Spacetime Energy",
                subtitle = "EV Charging Network",
                showBrandBadge = true,
                onNotificationClick = onNavigateToNotifications,
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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Greeting
            item {
                Column(modifier = Modifier.padding(horizontal = 2.dp)) {
                    Text(
                        text = "Hello, ${uiState.userName} 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MainText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
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

            // Search bar (navigates directly to interactive Explore map)
            item {
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChanged,
                    placeholder = "Search chargers, hubs, or cities...",
                    enabled = false,
                    trailingIcon = {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Primary.copy(alpha = 0.1f),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = "Open Map",
                                tint = Primary,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(18.dp)
                            )
                        }
                    },
                    modifier = Modifier.clickable { onNavigateToExplore() }
                )
            }

            // Quick Actions Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionButton(
                        icon = Icons.Default.QrCodeScanner,
                        label = "Scan QR",
                        accentColor = Primary,
                        onClick = onNavigateToQr,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = Icons.Default.NearMe,
                        label = "Nearby",
                        accentColor = Color(0xFF2563EB),
                        onClick = onNavigateToExplore,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = Icons.Default.ElectricBolt,
                        label = "Fast DC",
                        accentColor = Color(0xFFD97706),
                        onClick = onNavigateToExplore,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = Icons.Default.Bookmark,
                        label = "Saved",
                        accentColor = Color(0xFF7C3AED),
                        onClick = onNavigateToSavedStations,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Active Charging Banner if present
            uiState.activeSession?.let { session ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToActiveSession(session.sessionId) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.08f)),
                        border = BorderStroke(1.5.dp, Primary.copy(alpha = 0.65f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Success)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "ACTIVE CHARGING SESSION",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        letterSpacing = 0.5.sp,
                                        color = Primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${session.stationName} • ${session.energyKwh} kWh",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MainText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Primary
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Live",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
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
                    Column {
                        Text(
                            text = "Nearby Charging Hubs",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainText
                        )
                        Text(
                            text = "${uiState.nearbyStations.size} stations ready around you",
                            fontSize = 12.sp,
                            color = SecondaryText
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Primary.copy(alpha = 0.1f),
                        modifier = Modifier.clickable { onNavigateToExplore() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "View Map",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
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

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = Primary
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, Border),
        shadowElevation = 1.dp,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = accentColor.copy(alpha = 0.12f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MainText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
