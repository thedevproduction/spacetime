package com.spacetime.evcharging.feature.charging

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveChargingScreen(
    sessionId: String,
    onNavigateToSummary: (String) -> Unit,
    viewModel: ChargingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sessionId) {
        viewModel.resumeOrLoadSession(sessionId)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val session = uiState.activeSession

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Charging in Progress",
                subtitle = session?.stationName ?: "Live OCPP Telemetry"
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Live status pill
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Primary.copy(alpha = 0.12f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Success)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ACTIVE DISPENSE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp,
                        color = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Animated Charging Indicator
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Primary, PrimaryLight)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Active Charging",
                        tint = Color.White,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Delivering High-Power Energy",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${session?.stationName ?: "Highway EV Superhub"} • ${session?.chargerId ?: "CHG-01-DC"}",
                fontSize = 13.sp,
                color = SecondaryText
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Prominent Telemetry Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Energy Delivered",
                    value = "${session?.energyKwh ?: 22.45} kWh",
                    subtitle = "Real-time meter",
                    icon = Icons.Default.Bolt,
                    iconTint = Primary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Charging Power",
                    value = "${session?.powerKw ?: 52.6} kW",
                    subtitle = "DC Fast Dispense",
                    icon = Icons.Default.Speed,
                    iconTint = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Duration",
                    value = "32m 40s",
                    subtitle = "Started 10:30 AM",
                    icon = Icons.Default.Timer,
                    iconTint = Color(0xFFD97706),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Estimated Cost",
                    value = session?.estimatedAmount?.displayAmount ?: "₹314.30",
                    subtitle = "₹14.00/kWh rate",
                    icon = Icons.Default.Payments,
                    iconTint = Success,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f, fill = true))
            Spacer(modifier = Modifier.height(24.dp))

            // Actions
            Button(
                onClick = { viewModel.showStopConfirmation(true) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !uiState.isStopping
            ) {
                if (uiState.isStopping) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.5.dp, modifier = Modifier.size(24.dp))
                } else {
                    Icon(imageVector = Icons.Default.StopCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Stop Charging Session", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(imageVector = Icons.Default.SupportAgent, contentDescription = null, tint = SecondaryText)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Need Help or Emergency Stop?", color = SecondaryText)
            }

            Spacer(modifier = Modifier.navigationBarsPadding().height(20.dp))
        }

        // CUS-017 Stop Confirmation Bottom Sheet
        if (uiState.showStopConfirmSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.showStopConfirmation(false) },
                containerColor = CardBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text("Stop Charging?", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MainText)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Current dispensed energy: ${session?.energyKwh ?: 22.45} kWh (~${session?.estimatedAmount?.displayAmount ?: "₹314.30"}). Final meter reconciliation will be computed upon disconnect.",
                        fontSize = 14.sp,
                        color = SecondaryText
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    PrimaryButton(
                        text = "Confirm & Stop",
                        onClick = { viewModel.stopCharging(onNavigateToSummary) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SecondaryButton(
                        text = "Continue Charging",
                        onClick = { viewModel.showStopConfirmation(false) }
                    )
                }
            }
        }
    }
}
