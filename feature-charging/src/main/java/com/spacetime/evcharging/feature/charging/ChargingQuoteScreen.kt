package com.spacetime.evcharging.feature.charging

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
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
                shadowElevation = 10.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(color = Border, thickness = 0.75.dp)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Estimated Total", fontSize = 13.sp, color = SecondaryText)
                                Text("Payable on actual kWh used", fontSize = 11.sp, color = TertiaryText)
                            }
                            Text(
                                text = quote.estimatedTotal.displayAmount,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Primary
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        PrimaryButton(
                            text = "Continue to Payment",
                            onClick = onNavigateToStartConfirm,
                            leadingIcon = Icons.Default.ElectricBolt
                        )
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
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Charger details banner
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, Border),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                            Icon(imageVector = Icons.Default.EvStation, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Charger: ${quote.chargerId} (Gun #${quote.connectorId})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MainText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Highway EV Superhub Jabalpur",
                            fontSize = 13.sp,
                            color = SecondaryText
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SuccessLight
                    ) {
                        Text(
                            text = "Ready",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Success,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text("Tariff & Billing Breakdown", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
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
                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Primary.copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                            TariffRow(
                                label = "Estimated Energy (25 kWh)",
                                value = quote.estimatedTotal.displayAmount,
                                isBold = true
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transparency disclaimer
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Warning.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, Warning.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Final charges are based on actual energy delivered by the charger and applicable session durations.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MainText
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Payment Mode Selector
            Text("Select Payment Mode", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
            Spacer(modifier = Modifier.height(10.dp))

            PaymentModeOption(
                title = "Spacetime Wallet",
                subtitle = "Available Balance: ${quote.walletAvailable.displayAmount}",
                icon = Icons.Default.AccountBalanceWallet,
                badge = "Instant",
                isSelected = uiState.selectedPaymentMethod == "WALLET",
                onClick = { viewModel.selectPaymentMethod("WALLET") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            PaymentModeOption(
                title = "Direct UPI (Instant Auth)",
                subtitle = "PhonePe, Google Pay, Paytm, BHIM",
                icon = Icons.Default.QrCode2,
                isSelected = uiState.selectedPaymentMethod == "UPI",
                onClick = { viewModel.selectPaymentMethod("UPI") }
            )

            Spacer(modifier = Modifier.height(28.dp))
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
        Text(
            text = label,
            fontSize = 13.sp,
            color = if (isBold) MainText else SecondaryText,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = if (isBold) 15.sp else 13.sp,
            color = if (isBold) Primary else MainText,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.SemiBold
        )
    }
}

@Composable
fun PaymentModeOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badge: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Primary.copy(alpha = 0.08f) else CardBackground,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) Primary else Border),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(11.dp),
                color = if (isSelected) Primary.copy(alpha = 0.16f) else AppBackground,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Primary else SecondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MainText)
                    if (badge != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Primary.copy(alpha = 0.14f)
                        ) {
                            Text(
                                text = badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, fontSize = 12.sp, color = SecondaryText)
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = Primary)
            )
        }
    }
}
