package com.spacetime.evcharging.feature.charging

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun ChargingSummaryScreen(
    sessionId: String,
    onChargeAgain: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: ChargingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sessionId) {
        viewModel.loadSummary(sessionId)
    }

    if (uiState.isLoading) {
        LoadingScreen(message = "Reconciling meter values & generating invoice...")
        return
    }

    val summary = uiState.sessionSummary

    Scaffold(
        topBar = {
            SpacetimeTopBar(title = "Charging Receipt")
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = CircleShape,
                color = Success.copy(alpha = 0.15f),
                modifier = Modifier.size(68.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Success, modifier = Modifier.size(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("Session Completed", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MainText)
            Text("Your vehicle has been successfully charged.", fontSize = 13.sp, color = SecondaryText)

            Spacer(modifier = Modifier.height(24.dp))

            // Authoritative Receipt Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = summary?.stationName ?: "Highway EV Superhub Jabalpur", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
                    Text(text = "Charger #${summary?.chargerId ?: "CHG-01-DC"} • Gun #${summary?.connectorId ?: 1}", fontSize = 13.sp, color = SecondaryText)

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Border)
                    Spacer(modifier = Modifier.height(16.dp))

                    ReceiptItem(label = "Total Energy Dispensed", value = "${summary?.totalEnergyKwh ?: 22.45} kWh")
                    Spacer(modifier = Modifier.height(8.dp))
                    ReceiptItem(label = "Session Duration", value = summary?.durationFormatted ?: "32m 40s")
                    Spacer(modifier = Modifier.height(8.dp))
                    ReceiptItem(label = "Start & End Time", value = "${summary?.startTime ?: "10:30 AM"} - ${summary?.endTime ?: "11:02 AM"}")
                    Spacer(modifier = Modifier.height(8.dp))
                    ReceiptItem(label = "Energy Tariff", value = "${summary?.tariffRate?.displayAmount ?: "₹14.00"}/kWh")
                    Spacer(modifier = Modifier.height(8.dp))
                    ReceiptItem(label = "GST (18%)", value = summary?.taxAmount?.displayAmount ?: "₹56.57")

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Border)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Final Amount Paid", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MainText)
                        Text(
                            text = summary?.finalAmount?.displayAmount ?: "₹314.30",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Deducted from ${summary?.paymentMethod ?: "Spacetime Wallet"} (Status: ${summary?.paymentStatus?.name ?: "CAPTURED"})",
                        fontSize = 12.sp,
                        color = SecondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Invoice")
                }
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Back to Home",
                onClick = onNavigateToHome
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ReceiptItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = SecondaryText)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MainText)
    }
}
