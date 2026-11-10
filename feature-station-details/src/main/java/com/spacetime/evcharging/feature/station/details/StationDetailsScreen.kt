package com.spacetime.evcharging.feature.station.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun StationDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToChargerSelection: (String) -> Unit,
    viewModel: StationDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.isLoading) {
        LoadingScreen(message = "Loading station specs & live guns...")
        return
    }

    val station = uiState.station
    if (station == null) {
        ErrorState(message = uiState.error ?: "Station not found", onRetry = { viewModel.loadStationDetails("st_001") })
        return
    }

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Station Overview",
                showBack = true,
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            Surface(
                color = CardBackground,
                shadowElevation = 10.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(color = Border, thickness = 0.75.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Starting Tariff", fontSize = 12.sp, color = SecondaryText)
                            Text(
                                text = "${station.minimumTariff.displayAmount} / kWh",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Primary
                            )
                        }
                        Button(
                            onClick = { onNavigateToChargerSelection(station.id) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Select Charger", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
            }
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Card
            Card(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = station.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainText,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (uiState.isFavorite) Error else SecondaryText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = station.address, fontSize = 13.sp, color = SecondaryText)

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Warning, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${station.rating} (128 reviews)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                        }
                        Text("•", color = Border)
                        Text("${station.distanceKm} km away", fontSize = 13.sp, color = SecondaryText)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            val uri = Uri.parse("geo:${station.latitude},${station.longitude}?q=${station.latitude},${station.longitude}(${Uri.encode(station.name)})")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(Intent.createChooser(mapIntent, "Open in Maps"))
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Get Navigation Directions", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Real-time Gun Availability Summary
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Charger Equipment (${uiState.chargers.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
                Spacer(modifier = Modifier.height(10.dp))

                uiState.chargers.forEach { charger ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, Border),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(charger.displayName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MainText)
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Primary.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "${charger.ratedPowerKw.toInt()} kW ${charger.chargerType}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            charger.connectors.forEach { connector ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Gun #${connector.connectorId} (${connector.connectorType})", fontSize = 13.sp, color = MainText)
                                    }
                                    ChargerStatusChip(status = connector.status)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Station Amenities
                Text("Station Amenities", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    station.amenities.forEach { amenity ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CardBackground,
                            border = BorderStroke(1.dp, Border)
                        ) {
                            Text(
                                text = amenity,
                                fontSize = 12.sp,
                                color = MainText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Operating Hours
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = Primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Operating Schedule", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                            Text(station.operatingHours, fontSize = 12.sp, color = SecondaryText)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
