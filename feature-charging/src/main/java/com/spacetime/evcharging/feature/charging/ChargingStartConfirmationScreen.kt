package com.spacetime.evcharging.feature.charging

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun ChargingStartConfirmationScreen(
    quoteId: String,
    onNavigateBack: () -> Unit,
    onChargingStarted: (String) -> Unit,
    viewModel: ChargingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Start Charging Session",
                showBack = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Primary.copy(alpha = 0.12f),
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = null, tint = Primary, modifier = Modifier.size(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Ready to Power Up",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Please ensure your vehicle is securely connected to the charger before starting.",
                fontSize = 14.sp,
                color = SecondaryText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, Border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Success, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Connector firmly locked in charging port", fontSize = 13.sp, color = MainText)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Success, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Vehicle ignition switched off", fontSize = 13.sp, color = MainText)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Success, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Payment method authorized", fontSize = 13.sp, color = MainText)
                    }
                }
            }

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = uiState.error ?: "", color = Error, fontSize = 13.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = if (uiState.isLoading) "Contacting Charger..." else "Start Charging",
                onClick = { viewModel.startCharging(onChargingStarted) },
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
