package com.spacetime.evcharging.feature.provider.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun ProviderDashboardScreen(
    onNavigateToProfile: () -> Unit,
    viewModel: ProviderDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Service Provider Hub",
                subtitle = "Installations & AMC Operations",
                showBrandBadge = true,
                onProfileClick = onNavigateToProfile,
                onNotificationClick = {},
                notificationCount = 3
            )
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Loading service provider portal...")
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
                        text = uiState.providerName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                    Text(
                        text = "Equipment, Installations & AMC Tracker",
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
                        title = "Enterprise Clients",
                        value = "${d?.totalCustomers ?: 0}",
                        subtitle = "Site owners",
                        icon = Icons.Default.Business,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Chargers Installed",
                        value = "${d?.totalChargersInstalled ?: 0}",
                        subtitle = "Active in field",
                        icon = Icons.Default.EvStation,
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
                        title = "Active Installations",
                        value = "${d?.activeInstallations ?: 0}",
                        subtitle = "${d?.pendingInstallations ?: 0} pending surveys",
                        icon = Icons.Default.Construction,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Open Tickets",
                        value = "${d?.openServiceTickets ?: 0}",
                        subtitle = "${d?.warrantyExpiring ?: 0} warranties expiring",
                        icon = Icons.Default.SupportAgent,
                        iconTint = Warning,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
