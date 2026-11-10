package com.spacetime.evcharging.feature.site.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun SiteAdminDashboardScreen(
    onNavigateToProfile: () -> Unit,
    viewModel: SiteAdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Station Owner Portal",
                subtitle = "Live Site Operations",
                showBrandBadge = true,
                onProfileClick = onNavigateToProfile,
                onNotificationClick = {},
                notificationCount = 1
            )
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Loading owner telemetry...")
            return@Scaffold
        }

        val d = uiState.dashboard

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = uiState.businessName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                    Text(
                        text = "Today's Performance Overview",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Gross Revenue",
                        value = d?.grossRevenue?.displayAmount ?: "₹0.00",
                        subtitle = "+14% vs yesterday",
                        icon = Icons.Default.CurrencyRupee,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Energy Dispensed",
                        value = "${(d?.energyDeliveredWh ?: 0) / 1000} kWh",
                        subtitle = "Green energy",
                        icon = Icons.Default.Bolt,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Sessions Today",
                        value = "${d?.sessionCount ?: 0}",
                        subtitle = "Across 3 stations",
                        icon = Icons.Default.ElectricCar,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Unique Drivers",
                        value = "${d?.uniqueCustomerCount ?: 0}",
                        subtitle = "Active users",
                        icon = Icons.Default.People,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Text(
                    text = "Charger Network Health",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainText
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Success))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Available", fontSize = 14.sp, color = MainText, fontWeight = FontWeight.Medium)
                            }
                            Text("${d?.availableConnectors ?: 0} Guns", fontWeight = FontWeight.Bold, color = Success)
                        }
                        HorizontalDivider(color = Border.copy(alpha = 0.6f), thickness = 0.5.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Warning))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("In Use (Charging)", fontSize = 14.sp, color = MainText, fontWeight = FontWeight.Medium)
                            }
                            Text("${d?.occupiedConnectors ?: 0} Guns", fontWeight = FontWeight.Bold, color = Warning)
                        }
                        HorizontalDivider(color = Border.copy(alpha = 0.6f), thickness = 0.5.dp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if ((d?.faultedConnectors ?: 0) > 0) Error else TertiaryText))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Offline / Faulted", fontSize = 14.sp, color = MainText, fontWeight = FontWeight.Medium)
                            }
                            Text("${(d?.offlineConnectors ?: 0) + (d?.faultedConnectors ?: 0)} Guns", fontWeight = FontWeight.Bold, color = if ((d?.faultedConnectors ?: 0) > 0) Error else SecondaryText)
                        }
                    }
                }
            }
        }
    }
}
