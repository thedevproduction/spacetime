package com.spacetime.evcharging.feature.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun AddMoneyScreen(
    onNavigateBack: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val presetAmounts = listOf("100", "200", "500", "1000")

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Add Money to Wallet",
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text("Enter Top-up Amount", fontSize = 14.sp, color = SecondaryText)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedInput(
                value = uiState.topUpAmountText,
                onValueChange = viewModel::setTopUpAmount,
                label = "Amount (₹)",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Preset Amount Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presetAmounts.forEach { amount ->
                    val isSelected = uiState.topUpAmountText == amount
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Primary else CardBackground,
                        border = BorderStroke(1.dp, if (isSelected) Primary else Border),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setTopUpAmount(amount) }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = "+ ₹$amount",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MainText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text("Select Payment Gateway Mode", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
            Spacer(modifier = Modifier.height(12.dp))

            PaymentGatewayOption(
                title = "Instant UPI Intent (Recommended)",
                subtitle = "Google Pay, PhonePe, Paytm, BHIM",
                icon = Icons.Default.QrCode,
                isSelected = uiState.selectedPaymentMethod == "UPI",
                onClick = { viewModel.selectPaymentMethod("UPI") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            PaymentGatewayOption(
                title = "Credit or Debit Card",
                subtitle = "Visa, MasterCard, RuPay (RBI Compliant Tokenization)",
                icon = Icons.Default.CreditCard,
                isSelected = uiState.selectedPaymentMethod == "CARD",
                onClick = { viewModel.selectPaymentMethod("CARD") }
            )

            Spacer(modifier = Modifier.height(10.dp))

            PaymentGatewayOption(
                title = "Net Banking",
                subtitle = "All major Indian commercial banks",
                icon = Icons.Default.AccountBalance,
                isSelected = uiState.selectedPaymentMethod == "NETBANKING",
                onClick = { viewModel.selectPaymentMethod("NETBANKING") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardBackground,
                border = BorderStroke(1.dp, Border),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = SecondaryText, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Funds are protected and credited upon verified payment gateway webhook callbacks.",
                        fontSize = 12.sp,
                        color = SecondaryText
                    )
                }
            }

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = uiState.error ?: "", color = Error, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = if (uiState.isProcessingPayment) "Processing with Gateway..." else "Add ₹${uiState.topUpAmountText} to Wallet",
                onClick = {
                    viewModel.processTopUp(onSuccess = onNavigateBack)
                },
                enabled = uiState.topUpAmountText.isNotBlank() && (uiState.topUpAmountText.toLongOrNull() ?: 0) > 0,
                isLoading = uiState.isProcessingPayment
            )

            Spacer(modifier = Modifier.navigationBarsPadding().height(24.dp))
        }
    }
}

@Composable
fun PaymentGatewayOption(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
                .padding(14.dp),
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
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MainText)
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
