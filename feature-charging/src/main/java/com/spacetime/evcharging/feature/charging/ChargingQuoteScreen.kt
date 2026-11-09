package com.spacetime.evcharging.feature.charging

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
fun ChargingQuoteScreen(
    quoteId: String,
    onNavigateBack: () -> Unit,
    onNavigateToStartConfirm: () -> Unit,
    viewModel: ChargingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(quoteId) {
        viewModel.loadQuote(quoteId)
    }

    if (uiState.isLoading) {
        LoadingScreen(message = "Calculating tariff quote & reserving rate...")
        return
    }

    val quote = uiState.quote
    if (quote == null) {
        ErrorState(message = uiState.error ?: "Unable to fetch charging quote", onRetry = { viewModel.loadQuote(quoteId) })
        return
    }

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Charging Tariff Quote",
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Estimated Total", fontSize = 14.sp, color = SecondaryText)
                        Text(
                            text = quote.estimatedTotal.displayAmount,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryButton(
                        text = "Continue to Payment",
                        onClick = onNavigateToStartConfirm
                    )
                }
            }
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Charger details banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Primary.copy(alpha = 0.12f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.EvStation, contentDescription = null, tint = Primary)
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(text = "Charger: ${quote.chargerId} (Gun #${quote.connectorId})", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MainText)
                        Text(text = "Station: Highway EV Superhub Jabalpur", fontSize = 13.sp, color = SecondaryText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Tariff & Billing Breakdown", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, Border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TariffRow(label = "Energy Rate", value = "${quote.energyRatePerKwh.displayAmount} / kWh")
                    Spacer(modifier = Modifier.height(10.dp))
                    TariffRow(label = "Session Activation Fee", value = "₹0.00 (Free)")
                    Spacer(modifier = Modifier.height(10.dp))
                    TariffRow(label = "Parking Fee", value = "₹0.00")
                    Spacer(modifier = Modifier.height(10.dp))
                    TariffRow(label = "Idle Fee (after 10m grace)", value = "₹2.00 / min")
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Border)
                    Spacer(modifier = Modifier.height(12.dp))
                    TariffRow(label = "Estimated Energy (25 kWh)", value = quote.estimatedTotal.displayAmount, isBold = true)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transparency disclaimer
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Warning.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, Warning.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Warning, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Final charges are based on actual energy delivered by the charger and applicable session durations.",
                        fontSize = 12.sp,
                        color = MainText
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Payment Mode Selector
            Text("Select Payment Mode", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
            Spacer(modifier = Modifier.height(10.dp))

            PaymentModeOption(
                title = "Spacetime Wallet",
                subtitle = "Available Balance: ${quote.walletAvailable.displayAmount}",
                isSelected = uiState.selectedPaymentMethod == "WALLET",
                onClick = { viewModel.selectPaymentMethod("WALLET") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PaymentModeOption(
                title = "Direct UPI (Instant Auth)",
                subtitle = "PhonePe, Google Pay, Paytm, BHIM",
                isSelected = uiState.selectedPaymentMethod == "UPI",
                onClick = { viewModel.selectPaymentMethod("UPI") }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TariffRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 13.sp, color = if (isBold) MainText else SecondaryText, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(text = value, fontSize = 13.sp, color = if (isBold) Primary else MainText, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun PaymentModeOption(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) Primary.copy(alpha = 0.08f) else CardBackground,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) Primary else Border),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = Primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MainText)
                Text(text = subtitle, fontSize = 12.sp, color = SecondaryText)
            }
        }
    }
}
