package com.spacetime.evcharging.feature.station.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.spacetime.evcharging.data.model.Station

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreMapScreen(
    onNavigateToStationDetails: (String) -> Unit,
    viewModel: StationDiscoveryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = AppBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isMapMode) {
                // Interactive Map Canvas Simulation with station markers
                MapSimulationView(
                    stations = uiState.filteredStations,
                    selectedStation = uiState.selectedStation,
                    onSelectStation = viewModel::selectStation
                )
            } else {
                // List View (CUS-008)
                StationListScreenContent(
                    stations = uiState.filteredStations,
                    sortOption = uiState.sortOption,
                    onSortChanged = viewModel::updateSortOption,
                    onStationClick = onNavigateToStationDetails
                )
            }

            // Top Search & Filter Floating Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = viewModel::onSearchQueryChanged,
                        modifier = Modifier.weight(1f),
                        placeholder = "Search station, area, Jabalpur..."
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CardBackground,
                        shadowElevation = 3.dp,
                        modifier = Modifier
                            .size(52.dp)
                            .clickable { viewModel.showFilterSheet(true) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filters",
                                tint = if (uiState.filters.availableNowOnly || uiState.filters.open24Hours) Primary else MainText
                            )
                        }
                    }
                }
            }

            // Floating Toggle (Map / List) & Location Recenter
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = if (uiState.selectedStation != null && uiState.isMapMode) 220.dp else 24.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FloatingActionButton(
                    onClick = { viewModel.loadStations() },
                    containerColor = CardBackground,
                    contentColor = Primary,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Current Location")
                }

                ExtendedFloatingActionButton(
                    onClick = viewModel::toggleViewMode,
                    containerColor = Secondary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.isMapMode) Icons.Default.FormatListBulleted else Icons.Default.Map,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (uiState.isMapMode) "List View" else "Map View", fontWeight = FontWeight.SemiBold)
                }
            }

            // Selected Station Bottom Preview Card on Map (CUS-007)
            if (uiState.isMapMode && uiState.selectedStation != null) {
                val s = uiState.selectedStation!!
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = s.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MainText)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = s.address, fontSize = 12.sp, color = SecondaryText, maxLines = 1)
                            }
                            IconButton(onClick = { viewModel.selectStation(null) }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Dismiss", tint = SecondaryText)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${s.availableConnectors}/${s.totalConnectors} Connectors Available",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (s.availableConnectors > 0) Success else Error
                            )
                            Text(
                                text = "${s.minimumTariff.displayAmount}/kWh",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Directions")
                            }
                            Button(
                                onClick = { onNavigateToStationDetails(s.id) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary)
                            ) {
                                Text("View Details")
                            }
                        }
                    }
                }
            }
        }

        // Filter Bottom Sheet (CUS-009)
        if (uiState.isFilterSheetVisible) {
            StationFilterBottomSheet(
                currentFilters = uiState.filters,
                onApply = viewModel::applyFilters,
                onDismiss = { viewModel.showFilterSheet(false) },
                onReset = viewModel::resetFilters
            )
        }
    }
}

@Composable
fun MapSimulationView(
    stations: List<Station>,
    selectedStation: Station?,
    onSelectStation: (Station) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E9EC)) // Map background simulator
    ) {
        // Decorative map roads/grids
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
            repeat(6) {
                HorizontalDivider(color = Color.White.copy(alpha = 0.6f), thickness = 2.dp)
            }
        }

        // Render interactive markers on map canvas
        stations.forEachIndexed { index, station ->
            val isSelected = selectedStation?.id == station.id
            val (xOffset, yOffset) = when (index % 3) {
                0 -> 100.dp to 220.dp
                1 -> 250.dp to 380.dp
                else -> 120.dp to 500.dp
            }

            Box(
                modifier = Modifier
                    .offset(x = xOffset, y = yOffset)
                    .clickable { onSelectStation(station) }
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) Secondary else CardBackground,
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        if (station.availableConnectors > 0) Success else Warning
                    ),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (station.availableConnectors > 0) Success else Warning)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${station.minimumTariff.displayAmount} • ${station.name.take(12)}..",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MainText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StationListScreenContent(
    stations: List<Station>,
    sortOption: StationSortOption,
    onSortChanged: (StationSortOption) -> Unit,
    onStationClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp)
    ) {
        // Sort filter chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StationSortOption.entries.forEach { option ->
                val isSelected = sortOption == option
                FilterChip(
                    selected = isSelected,
                    onClick = { onSortChanged(option) },
                    label = { Text(option.displayName, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Primary.copy(alpha = 0.15f),
                        selectedLabelColor = Primary
                    )
                )
            }
        }

        // Station Cards List
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(stations.size) { index ->
                val s = stations[index]
                StationCard(
                    name = s.name,
                    address = s.address,
                    distance = "${s.distanceKm} km",
                    availableConnectors = s.availableConnectors,
                    totalConnectors = s.totalConnectors,
                    priceRange = "${s.minimumTariff.displayAmount}/kWh",
                    rating = s.rating,
                    onClick = { onStationClick(s.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationFilterBottomSheet(
    currentFilters: StationFilterState,
    onApply: (StationFilterState) -> Unit,
    onDismiss: () -> Unit,
    onReset: () -> Unit
) {
    var availableNow by remember { mutableStateOf(currentFilters.availableNowOnly) }
    var open24h by remember { mutableStateOf(currentFilters.open24Hours) }
    var maxDistance by remember { mutableFloatStateOf(currentFilters.maxDistanceKm) }
    var selectedCategory by remember { mutableStateOf(currentFilters.selectedCategory) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = CardBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Station Filters", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                TextButton(onClick = onReset) {
                    Text("Reset All", color = Error, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Switch options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Available Now Only", fontSize = 14.sp, color = MainText)
                Switch(
                    checked = availableNow,
                    onCheckedChange = { availableNow = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = Primary.copy(alpha = 0.3f))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Open 24 Hours", fontSize = 14.sp, color = MainText)
                Switch(
                    checked = open24h,
                    onCheckedChange = { open24h = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Primary, checkedTrackColor = Primary.copy(alpha = 0.3f))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Charger Category", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MainText)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("ALL" to "All", "DC" to "DC Fast", "AC" to "AC Standard").forEach { (key, label) ->
                    val isSelected = selectedCategory == key
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Primary else AppBackground,
                        modifier = Modifier.clickable { selectedCategory = key }
                    ) {
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White else MainText,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Max Distance: ${maxDistance.toInt()} km", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MainText)
            Slider(
                value = maxDistance,
                onValueChange = { maxDistance = it },
                valueRange = 5f..50f,
                colors = SliderDefaults.colors(thumbColor = Primary, activeTrackColor = Primary)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Apply Filters",
                onClick = {
                    onApply(
                        currentFilters.copy(
                            availableNowOnly = availableNow,
                            open24Hours = open24h,
                            maxDistanceKm = maxDistance,
                            selectedCategory = selectedCategory
                        )
                    )
                }
            )
        }
    }
}
