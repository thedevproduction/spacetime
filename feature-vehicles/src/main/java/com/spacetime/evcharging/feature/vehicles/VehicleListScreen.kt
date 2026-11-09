package com.spacetime.evcharging.feature.vehicles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
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
import com.spacetime.evcharging.data.model.Vehicle

@Composable
fun VehicleListScreen(
    onNavigateToAddVehicle: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: VehiclesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "My EV Garage",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddVehicle,
                containerColor = Primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Vehicle")
            }
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Loading your registered vehicles...")
            return@Scaffold
        }

        if (uiState.vehicles.isEmpty()) {
            EmptyState(
                title = "No EV Registered",
                message = "Add your electric vehicle to check charger compatibility and view estimated charging speeds.",
                actionText = "Add Vehicle",
                onAction = onNavigateToAddVehicle,
                modifier = Modifier.padding(padding)
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Registered Vehicles (${uiState.vehicles.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MainText
                )
            }

            items(uiState.vehicles) { vehicle ->
                VehicleCard(
                    vehicle = vehicle,
                    onSetDefault = { viewModel.setDefaultVehicle(vehicle.id) },
                    onDelete = { viewModel.deleteVehicle(vehicle.id) }
                )
            }
        }
    }
}

@Composable
fun VehicleCard(
    vehicle: Vehicle,
    onSetDefault: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(
            1.5.dp,
            if (vehicle.isDefault) Primary else Border
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Primary.copy(alpha = 0.12f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = vehicle.nickname,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainText
                        )
                        Text(
                            text = "${vehicle.manufacturer} • ${vehicle.model}",
                            fontSize = 13.sp,
                            color = SecondaryText
                        )
                    }
                }

                if (vehicle.isDefault) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Success.copy(alpha = 0.15f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Success,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Default",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Success
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Border)
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Battery", fontSize = 12.sp, color = SecondaryText)
                    Text("${vehicle.batteryCapacityKwh} kWh", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                }
                Column {
                    Text("Connectors", fontSize = 12.sp, color = SecondaryText)
                    Text(vehicle.connectorTypes.joinToString(", "), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                }
                Column {
                    Text("Max DC Fast", fontSize = 12.sp, color = SecondaryText)
                    Text("${vehicle.maxDcPowerKw} kW", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                }
            }

            if (!vehicle.isDefault) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = SecondaryText)
                    }
                    TextButton(onClick = onSetDefault) {
                        Text("Set as Default", color = Primary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
