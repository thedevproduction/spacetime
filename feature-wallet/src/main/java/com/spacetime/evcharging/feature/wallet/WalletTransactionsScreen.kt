package com.spacetime.evcharging.feature.wallet

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*

@Composable
fun WalletTransactionsScreen(
    onBackClick: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val filterOptions = listOf(
        "ALL" to "All",
        "TOPUP" to "Top-ups",
        "CHARGE" to "Charging Charges",
        "REFUND" to "Cashback / Refunds"
    )

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "Wallet Ledger",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = AppBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { (key, label) ->
                    val isSelected = uiState.selectedFilter == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectFilter(key) },
                        label = { Text(label, fontSize = 12.sp, maxLines = 1, softWrap = false) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Primary.copy(alpha = 0.15f),
                            selectedLabelColor = Primary
                        )
                    )
                }
            }

            if (uiState.filteredTransactions.isEmpty()) {
                EmptyState(
                    title = "No Transactions Found",
                    message = "No financial activity recorded under the selected filter."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.filteredTransactions) { tx ->
                        TransactionCard(transaction = tx)
                    }
                }
            }
        }
    }
}
