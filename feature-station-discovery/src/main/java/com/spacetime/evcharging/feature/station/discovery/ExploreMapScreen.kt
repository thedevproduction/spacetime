package com.spacetime.evcharging.feature.station.discovery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.spacetime.evcharging.core.common.model.ChargerStatus
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
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) {
                        viewModel.updateUserLocation(loc.latitude, loc.longitude)
                        Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: SecurityException) {
                // Handled gracefully
            }
        }
    }

    fun requestLocationUpdate() {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fine || coarse) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    if (loc != null) {
                        viewModel.updateUserLocation(loc.latitude, loc.longitude)
                        Toast.makeText(context, "Centered on your location", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.loadStations(uiState.userLatitude, uiState.userLongitude)
                    }
                }
            } catch (e: SecurityException) {
                viewModel.loadStations(uiState.userLatitude, uiState.userLongitude)
            }
        } else {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    Scaffold(
        containerColor = AppBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isMapMode) {
                // Real Map Integration: Interactive High-Res Map with Google Maps toggle
                if (uiState.mapEngine == MapEngine.GOOGLE_MAPS) {
                    GoogleMapsView(
                        stations = uiState.filteredStations,
                        selectedStation = uiState.selectedStation,
                        userLatitude = uiState.userLatitude,
                        userLongitude = uiState.userLongitude,
                        onSelectStation = viewModel::selectStation
                    )
                } else {
                    RealInteractiveMapView(
                        stations = uiState.filteredStations,
                        selectedStation = uiState.selectedStation,
                        userLatitude = uiState.userLatitude,
                        userLongitude = uiState.userLongitude,
                        onSelectStation = viewModel::selectStation
                    )
                }
            } else {
                // Full List View (CUS-008)
                StationListScreenContent(
                    stations = uiState.filteredStations,
                    sortOption = uiState.sortOption,
                    onSortChanged = viewModel::updateSortOption,
                    onStationClick = onNavigateToStationDetails
                )
            }

            // Top Floating Search, Filters & Quick Filter Chips
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = viewModel::onSearchQueryChanged,
                        modifier = Modifier.weight(1f),
                        placeholder = "Search station, Jabalpur, Bhopal..."
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CardBackground,
                        shadowElevation = 4.dp,
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

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Filter Chips row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickFilterChip(
                        label = "All (${uiState.stations.size})",
                        isSelected = uiState.activeQuickFilter == "ALL",
                        onClick = { viewModel.setQuickFilter("ALL") }
                    )
                    QuickFilterChip(
                        label = "⚡ Fast DC",
                        isSelected = uiState.activeQuickFilter == "FAST_DC",
                        onClick = { viewModel.setQuickFilter("FAST_DC") }
                    )
                    QuickFilterChip(
                        label = "🟢 Available Now",
                        isSelected = uiState.activeQuickFilter == "AVAILABLE_NOW",
                        onClick = { viewModel.setQuickFilter("AVAILABLE_NOW") }
                    )
                    QuickFilterChip(
                        label = "🕒 24/7 Open",
                        isSelected = uiState.activeQuickFilter == "24_HOURS",
                        onClick = { viewModel.setQuickFilter("24_HOURS") }
                    )
                    QuickFilterChip(
                        label = "₹ Lowest Tariff",
                        isSelected = uiState.activeQuickFilter == "CHEAP",
                        onClick = { viewModel.setQuickFilter("CHEAP") }
                    )
                }
            }

            // Floating Controls on Map: Recenter, Toggle Map Engine, Toggle View
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        bottom = if (uiState.selectedStation != null && uiState.isMapMode) 250.dp else 24.dp,
                        end = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Map Engine Switcher (Leaflet OSM / Google Maps)
                if (uiState.isMapMode) {
                    FloatingActionButton(
                        onClick = { viewModel.toggleMapEngine() },
                        containerColor = CardBackground,
                        contentColor = Secondary,
                        shape = CircleShape,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Icon(
                            imageVector = if (uiState.mapEngine == MapEngine.GOOGLE_MAPS) Icons.Default.Layers else Icons.Outlined.Layers,
                            contentDescription = "Switch Map Engine",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Recenter / Locate Me
                FloatingActionButton(
                    onClick = { requestLocationUpdate() },
                    containerColor = CardBackground,
                    contentColor = Primary,
                    shape = CircleShape,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Current Location")
                }

                // Map / List Toggle
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

            // Selected Station Bottom Preview Card (CUS-007)
            AnimatedVisibility(
                visible = uiState.isMapMode && uiState.selectedStation != null,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                uiState.selectedStation?.let { station ->
                    SelectedStationPreviewCard(
                        station = station,
                        onDismiss = { viewModel.selectStation(null) },
                        onNavigateToDetails = { onNavigateToStationDetails(station.id) },
                        onGetDirections = {
                            val uri = Uri.parse("geo:${station.latitude},${station.longitude}?q=${station.latitude},${station.longitude}(${Uri.encode(station.name)})")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(Intent.createChooser(mapIntent, "Open in Maps"))
                        }
                    )
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
fun QuickFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Primary else CardBackground,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) Primary else Border
        ),
        shadowElevation = if (isSelected) 3.dp else 1.dp,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else MainText,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
        )
    }
}

@Composable
fun SelectedStationPreviewCard(
    station: Station,
    onDismiss: () -> Unit,
    onNavigateToDetails: () -> Unit,
    onGetDirections: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Name & Close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = station.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = MainText
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Hub",
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${station.distanceKm} km away • ${station.address}",
                        fontSize = 12.sp,
                        color = SecondaryText,
                        maxLines = 1
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss",
                        tint = SecondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Status metrics row: Availability, Tariff, Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (station.availableConnectors > 0) Success else Warning)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${station.availableConnectors} / ${station.totalConnectors} Available",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (station.availableConnectors > 0) Success else Warning
                    )
                }

                Text(
                    text = "${station.minimumTariff.displayAmount}/kWh",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${station.rating}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF92400E)
                    )
                }
            }

            // Amenities Tags
            if (station.amenities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    station.amenities.take(3).forEach { amenity ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AppBackground
                        ) {
                            Text(
                                text = amenity,
                                fontSize = 11.sp,
                                color = SecondaryText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Directions & Charge Now
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onGetDirections,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Secondary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Directions,
                        contentDescription = "Navigate",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Directions", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onNavigateToDetails,
                    modifier = Modifier.weight(1.3f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "Charge",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Charge Here", fontWeight = FontWeight.Bold, color = Color.White)
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
            .padding(top = 110.dp)
    ) {
        // Sort filter chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
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

        if (stations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No charging stations found matching your filters.",
                    color = SecondaryText,
                    fontSize = 14.sp
                )
            }
        } else {
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
