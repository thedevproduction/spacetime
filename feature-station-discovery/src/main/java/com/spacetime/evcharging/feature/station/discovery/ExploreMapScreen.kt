package com.spacetime.evcharging.feature.station.discovery

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
    val focusManager = LocalFocusManager.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    fun fetchUserLocation() {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!fine && !coarse) return

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    viewModel.updateUserLocation(loc.latitude, loc.longitude)
                } else {
                    val req = CurrentLocationRequest.Builder()
                        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .build()
                    fusedLocationClient.getCurrentLocation(req, null).addOnSuccessListener { freshLoc ->
                        if (freshLoc != null) {
                            viewModel.updateUserLocation(freshLoc.latitude, freshLoc.longitude)
                        } else {
                            viewModel.triggerRecenter()
                            viewModel.loadStations(uiState.userLatitude, uiState.userLongitude)
                        }
                    }.addOnFailureListener {
                        viewModel.triggerRecenter()
                        viewModel.loadStations(uiState.userLatitude, uiState.userLongitude)
                    }
                }
            }.addOnFailureListener {
                viewModel.triggerRecenter()
                viewModel.loadStations(uiState.userLatitude, uiState.userLongitude)
            }
        } catch (e: SecurityException) {
            viewModel.loadStations(uiState.userLatitude, uiState.userLongitude)
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            fetchUserLocation()
            Toast.makeText(context, "Location permission granted", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.loadStations(uiState.userLatitude, uiState.userLongitude)
        }
    }

    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (fine || coarse) {
            fetchUserLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = AppBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isMapMode) {
                when (uiState.mapEngine) {
                    MapEngine.OSM_STREET -> {
                        OsmRealMapView(
                            stations = uiState.filteredStations,
                            selectedStation = uiState.selectedStation,
                            userLatitude = uiState.userLatitude,
                            userLongitude = uiState.userLongitude,
                            recenterTrigger = uiState.recenterTrigger,
                            useVoyagerStyle = false,
                            onSelectStation = viewModel::selectStation
                        )
                    }
                    MapEngine.OSM_CLEAN -> {
                        OsmRealMapView(
                            stations = uiState.filteredStations,
                            selectedStation = uiState.selectedStation,
                            userLatitude = uiState.userLatitude,
                            userLongitude = uiState.userLongitude,
                            recenterTrigger = uiState.recenterTrigger,
                            useVoyagerStyle = true,
                            onSelectStation = viewModel::selectStation
                        )
                    }
                    MapEngine.GOOGLE_MAPS -> {
                        GoogleMapsView(
                            stations = uiState.filteredStations,
                            selectedStation = uiState.selectedStation,
                            userLatitude = uiState.userLatitude,
                            userLongitude = uiState.userLongitude,
                            onSelectStation = viewModel::selectStation,
                            onFallbackToStreetMap = { viewModel.setMapEngine(MapEngine.OSM_STREET) }
                        )
                    }
                    MapEngine.OFFLINE_VECTOR -> {
                        NativeMapView(
                            stations = uiState.filteredStations,
                            selectedStation = uiState.selectedStation,
                            userLatitude = uiState.userLatitude,
                            userLongitude = uiState.userLongitude,
                            onSelectStation = viewModel::selectStation
                        )
                    }
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
                    .statusBarsPadding()
                    .padding(top = 8.dp)
            ) {
                val hasActiveFilters = uiState.filters.availableNowOnly || uiState.filters.open24Hours || uiState.filters.selectedCategory != "ALL"

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = viewModel::onSearchQueryChanged,
                        modifier = Modifier.weight(1f),
                        placeholder = "Search stations, hubs, or areas..."
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (hasActiveFilters) Primary.copy(alpha = 0.12f) else CardBackground,
                        border = BorderStroke(1.dp, if (hasActiveFilters) Primary else Border),
                        shadowElevation = 3.dp,
                        modifier = Modifier
                            .size(54.dp)
                            .clickable { viewModel.showFilterSheet(true) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Filters",
                                tint = if (hasActiveFilters) Primary else MainText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Filter Chips row (edge-to-edge scrolling without clipping)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    QuickFilterChip(
                        label = "All (${uiState.filteredStations.size})",
                        isSelected = uiState.activeQuickFilter == "ALL",
                        onClick = { viewModel.setQuickFilter("ALL") }
                    )
                    QuickFilterChip(
                        label = "Fast DC",
                        leadingIcon = Icons.Default.Bolt,
                        iconTint = Color(0xFFD97706),
                        isSelected = uiState.activeQuickFilter == "FAST_DC",
                        onClick = { viewModel.setQuickFilter("FAST_DC") }
                    )
                    QuickFilterChip(
                        label = "Available Now",
                        leadingIcon = Icons.Default.CheckCircle,
                        iconTint = Success,
                        isSelected = uiState.activeQuickFilter == "AVAILABLE_NOW",
                        onClick = { viewModel.setQuickFilter("AVAILABLE_NOW") }
                    )
                    QuickFilterChip(
                        label = "24/7 Open",
                        leadingIcon = Icons.Default.Schedule,
                        iconTint = Color(0xFF2563EB),
                        isSelected = uiState.activeQuickFilter == "24_HOURS",
                        onClick = { viewModel.setQuickFilter("24_HOURS") }
                    )
                    QuickFilterChip(
                        label = "Lowest Tariff",
                        leadingIcon = Icons.Default.CurrencyRupee,
                        iconTint = Primary,
                        isSelected = uiState.activeQuickFilter == "CHEAP",
                        onClick = { viewModel.setQuickFilter("CHEAP") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Live Search Suggestions Dropdown on Map
                if (uiState.isMapMode && uiState.searchQuery.isNotBlank() && uiState.selectedStation == null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, Border),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            if (uiState.filteredStations.isEmpty()) {
                                Text(
                                    text = "No hubs matching \"${uiState.searchQuery}\"",
                                    fontSize = 13.sp,
                                    color = SecondaryText,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                uiState.filteredStations.take(4).forEachIndexed { index, station ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                focusManager.clearFocus()
                                                viewModel.selectStation(station)
                                            }
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.EvStation,
                                                contentDescription = null,
                                                tint = Primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = station.name,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MainText,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = station.address,
                                                    fontSize = 11.sp,
                                                    color = SecondaryText,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${station.distanceKm} km",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Primary
                                        )
                                    }
                                    if (index < uiState.filteredStations.take(4).lastIndex) {
                                        HorizontalDivider(color = Border, thickness = 0.5.dp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Floating Controls: Layer Switcher, Recenter, Toggle View
            val controlsBottomPadding by animateDpAsState(
                targetValue = if (uiState.selectedStation != null && uiState.isMapMode) 256.dp else 20.dp,
                label = "controlsBottom"
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = controlsBottomPadding, end = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (uiState.isMapMode) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = CardBackground.copy(alpha = 0.96f),
                        border = BorderStroke(1.dp, Border),
                        shadowElevation = 4.dp,
                        modifier = Modifier.clickable { viewModel.cycleMapEngine() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = "Switch Map Layer",
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = uiState.mapEngine.displayName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MainText
                            )
                        }
                    }

                    FloatingActionButton(
                        onClick = {
                            val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            if (fine || coarse) {
                                fetchUserLocation()
                                viewModel.triggerRecenter()
                                Toast.makeText(context, "Centering on your location...", Toast.LENGTH_SHORT).show()
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                                )
                            }
                        },
                        containerColor = CardBackground,
                        contentColor = Primary,
                        shape = CircleShape,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Current Location")
                    }
                }

                // Map / List Toggle Button
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
    onClick: () -> Unit,
    leadingIcon: ImageVector? = null,
    iconTint: Color = Primary
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Primary else CardBackground,
        border = BorderStroke(
            1.dp,
            if (isSelected) Primary else Border
        ),
        shadowElevation = if (isSelected) 3.dp else 2.dp,
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 7.dp)
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else iconTint,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) Color.White else MainText
            )
        }
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
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Border)
            )
            Spacer(modifier = Modifier.height(10.dp))

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
                            color = MainText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (station.availableConnectors > 0) SuccessLight else WarningLight
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (station.availableConnectors > 0) Success else Warning)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${station.availableConnectors}/${station.totalConnectors} Available",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (station.availableConnectors > 0) Success else Color(0xFF92400E)
                        )
                    }
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

            if (station.amenities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
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

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onGetDirections,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
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
                    modifier = Modifier
                        .weight(1.3f)
                        .height(46.dp),
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
            .statusBarsPadding()
            .padding(top = 122.dp)
    ) {
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
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 92.dp),
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
