package com.spacetime.evcharging.feature.station.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun ChargerSelectionScreen(
    stationId: String,
    onNavigateBack: () -> Unit,
    onContinueToQuote: (String, String, Int) -> Unit,
    viewModel: StationDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Select Charger & Gun",
                showBack = true,
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            Surface(
                color = CardBackground,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    PrimaryButton(
                        text = "Continue to Quote",
                        onClick = {
                            val c = uiState.selectedCharger
                            val gun = uiState.selectedConnector
                            if (c != null && gun != null) {
                                onContinueToQuote(stationId, c.chargerId, gun.connectorId)
                            }
                        },
                        enabled = uiState.selectedConnector != null && uiState.selectedConnector!!.status.isAvailableToCharge
                    )
                }
            }
        },
        containerColor = AppBackground
    ) { padding ->
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
                        text = "Available Charging Guns",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                    Text(
                        text = "Choose an available connector to view live pricing and start charging.",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }
            }

            items(uiState.chargers) { charger ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${charger.displayName} (${charger.ratedPowerKw.toInt()} kW)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainText
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        charger.connectors.forEach { connector ->
                            val isSelected = uiState.selectedCharger?.chargerId == charger.chargerId &&
                                    uiState.selectedConnector?.connectorId == connector.connectorId
                            val isAvailable = connector.status.isAvailableToCharge

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = when {
                                    isSelected -> Primary.copy(alpha = 0.08f)
                                    !isAvailable -> AppBackground
                                    else -> CardBackground
                                },
                                border = BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) Primary else Border
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable(enabled = isAvailable) {
                                        viewModel.selectConnector(charger, connector)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.ElectricBolt,
                                            contentDescription = null,
                                            tint = if (isSelected) Primary else if (isAvailable) SecondaryText else Error,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "Gun #${connector.connectorId} • ${connector.connectorType}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isAvailable) MainText else SecondaryText
                                            )
                                            Text(
                                                text = "Standard Tariff: ₹14.00/kWh",
                                                fontSize = 12.sp,
                                                color = SecondaryText
                                            )
                                        }
                                    }
                                    ChargerStatusChip(status = connector.status)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
