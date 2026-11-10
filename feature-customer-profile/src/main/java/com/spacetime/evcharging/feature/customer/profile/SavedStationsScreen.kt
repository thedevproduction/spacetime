package com.spacetime.evcharging.feature.customer.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun SavedStationsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToStationDetails: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Saved Stations",
                showBack = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.savedStations.isEmpty()) {
            EmptyState(
                title = "No Saved Stations",
                message = "Bookmark stations on the map or station overview to quickly check gun availability.",
                modifier = Modifier.padding(padding)
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(uiState.savedStations) { station ->
                StationCard(
                    name = station.name,
                    address = station.address,
                    distance = "${station.distanceKm} km",
                    availableConnectors = station.availableConnectors,
                    totalConnectors = station.totalConnectors,
                    priceRange = "${station.minimumTariff.displayAmount}/kWh",
                    rating = station.rating,
                    onClick = { onNavigateToStationDetails(station.id) }
                )
            }
        }
    }
}
