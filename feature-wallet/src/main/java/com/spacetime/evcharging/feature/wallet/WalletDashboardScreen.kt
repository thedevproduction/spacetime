package com.spacetime.evcharging.feature.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.data.model.WalletTransaction

@Composable
fun WalletDashboardScreen(
    onNavigateToAddMoney: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SpacetimeTopBar(title = "Spacetime Wallet")
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading && uiState.wallet == null) {
            LoadingScreen(message = "Loading wallet balance & ledger...")
            return@Scaffold
        }

        val wallet = uiState.wallet

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Balance Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Secondary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Available Spendable Balance",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Primary
                            ) {
                                Text(
                                    text = "Active",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = wallet?.availableBalance?.displayAmount ?: "₹0.00",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Promotional Credits", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                                Text(wallet?.promotionalBalance?.displayAmount ?: "₹0.00", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }
                            Button(
                                onClick = onNavigateToAddMoney,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Add Money", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Quick Stats Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Monthly Top-ups",
                        value = "₹1,500",
                        subtitle = "Via UPI Autopay",
                        icon = Icons.Default.ArrowDownward,
                        iconTint = Success,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Charging Deductions",
                        value = "₹559",
                        subtitle = "Across 3 sessions",
                        icon = Icons.Default.Bolt,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Recent Transactions Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary,
                        modifier = Modifier.clickable { onNavigateToTransactions() }
                    )
                }
            }

            // Transaction Items
            items(uiState.transactions.take(4)) { tx ->
                TransactionCard(transaction = tx)
            }
        }
    }
}

@Composable
fun TransactionCard(transaction: WalletTransaction) {
    val isCredit = transaction.amount.amountMinor > 0

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, Border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = if (isCredit) Success.copy(alpha = 0.12f) else Secondary.copy(alpha = 0.08f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isCredit) Icons.Default.ArrowDownward else Icons.Default.Bolt,
                            contentDescription = null,
                            tint = if (isCredit) Success else Secondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = transaction.type, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MainText)
                    Text(text = transaction.timestamp, fontSize = 12.sp, color = SecondaryText)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isCredit) "+" else ""}${transaction.amount.displayAmount}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (isCredit) Success else MainText
                )
                Text(text = transaction.status, fontSize = 11.sp, color = SecondaryText)
            }
        }
    }
}
