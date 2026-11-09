package com.spacetime.evcharging.feature.site.revenue

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.data.model.SettlementRecord
import com.spacetime.evcharging.data.model.SiteAdminRevenue
import com.spacetime.evcharging.data.repository.SiteAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SiteRevenueUiState(
    val revenue: SiteAdminRevenue? = null,
    val settlements: List<SettlementRecord> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class SiteRevenueViewModel @Inject constructor(
    private val siteAdminRepository: SiteAdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SiteRevenueUiState())
    val uiState: StateFlow<SiteRevenueUiState> = _uiState.asStateFlow()

    init {
        loadRevenue()
    }

    fun loadRevenue() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val revRes = siteAdminRepository.getRevenueSummary()
            val setRes = siteAdminRepository.getSettlements()

            if (revRes is AppResult.Success && setRes is AppResult.Success) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        revenue = revRes.data,
                        settlements = setRes.data
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load revenue statements") }
            }
        }
    }
}

@Composable
fun SiteRevenueScreen(
    viewModel: SiteRevenueViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Revenue & Settlements") },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading && uiState.revenue == null) {
            LoadingScreen(message = "Compiling financial statements & gateway deductions...")
            return@Scaffold
        }

        val rev = uiState.revenue

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Net Payable Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Secondary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        Text("Net Settlement Payable", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = rev?.netSettlement?.displayAmount ?: "₹0.00",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Paid Settlements", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                                Text(rev?.paidSettlements?.displayAmount ?: "₹0.00", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            }
                            Column {
                                Text("Pending Clearing", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                                Text(rev?.pendingSettlements?.displayAmount ?: "₹0.00", fontWeight = FontWeight.Bold, color = Warning, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // Deductions & Breakdown
            item {
                Text("Deduction Accounting Breakdown", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        RevenueRow(label = "Gross Charging Collection", value = rev?.grossChargingValue?.displayAmount ?: "₹0.00", isBold = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        RevenueRow(label = "Platform Commission (10%)", value = "- ${rev?.platformCommission?.displayAmount ?: "₹0.00"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        RevenueRow(label = "Payment Gateway Fee (2%)", value = "- ${rev?.gatewayFees?.displayAmount ?: "₹0.00"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        RevenueRow(label = "GST on Convenience (18%)", value = "- ${rev?.taxAmount?.displayAmount ?: "₹0.00"}")
                    }
                }
            }

            // Settlement History
            item {
                Text("Settlement Statements", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MainText)
            }

            items(uiState.settlements) { settlement ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(settlement.settlementId, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MainText)
                            Text(settlement.period, fontSize = 12.sp, color = SecondaryText)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Net: ${settlement.netAmount.displayAmount} (Gross: ${settlement.grossAmount.displayAmount})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Primary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (settlement.status == "PAID") Success.copy(alpha = 0.12f) else Warning.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = settlement.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (settlement.status == "PAID") Success else Warning,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RevenueRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = if (isBold) MainText else SecondaryText, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = if (isBold) Primary else MainText)
    }
}
