package com.spacetime.evcharging.feature.charging

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.data.model.ActiveChargingSession
import com.spacetime.evcharging.data.model.SessionSummary
import com.spacetime.evcharging.data.repository.ChargingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyChargingUiState(
    val activeSession: ActiveChargingSession? = null,
    val history: List<SessionSummary> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class MyChargingViewModel @Inject constructor(
    private val chargingRepository: ChargingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyChargingUiState())
    val uiState: StateFlow<MyChargingUiState> = _uiState.asStateFlow()

    init {
        loadSessions()
    }

    fun loadSessions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val active = (chargingRepository.getActiveSession() as? AppResult.Success)?.data
            when (val res = chargingRepository.getSessionHistory()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            activeSession = active,
                            history = res.data,
                            isLoading = false
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            activeSession = active,
                            isLoading = false,
                            error = res.message
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyChargingSessionsScreen(
    onOpenActiveSession: (String) -> Unit,
    onOpenSessionSummary: (String) -> Unit,
    onScanQrToCharge: () -> Unit,
    viewModel: MyChargingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSessions()
    }

    Scaffold(
        topBar = {
            SpacetimeTopBar(
                title = "My Charging Sessions",
                subtitle = "Live telemetry & billing receipts"
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onScanQrToCharge,
                containerColor = Primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scan & Charge", fontWeight = FontWeight.SemiBold)
            }
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading && uiState.history.isEmpty()) {
            LoadingScreen(message = "Loading charging history...")
            return@Scaffold
        }

        val totalEnergy = uiState.history.sumOf { it.totalEnergyKwh }
        val totalSessions = uiState.history.size

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Summary KPI Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Completed",
                        value = "$totalSessions",
                        subtitle = "Charging sessions",
                        icon = Icons.Default.EvStation,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Energy Added",
                        value = "${String.format("%.1f", totalEnergy)} kWh",
                        subtitle = "Clean mobility",
                        icon = Icons.Default.Bolt,
                        iconTint = Success,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Active Session Card
            uiState.activeSession?.let { active ->
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenActiveSession(active.sessionId) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Primary.copy(alpha = 0.08f)),
                        border = BorderStroke(1.5.dp, Primary)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(9.dp)
                                            .clip(CircleShape)
                                            .background(Success)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "LIVE CHARGING SESSION",
                                        fontSize = 11.sp,
                                        letterSpacing = 0.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Primary.copy(alpha = 0.14f)
                                ) {
                                    Text(
                                        text = "${active.powerKw} kW",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = active.stationName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MainText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${active.chargerId} • Connector #${active.connectorId}",
                                fontSize = 12.sp,
                                color = SecondaryText
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${String.format("%.2f", active.energyKwh)} kWh • Est. ${active.estimatedAmount.displayAmount}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MainText
                                )
                                Button(
                                    onClick = { onOpenActiveSession(active.sessionId) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("View Live", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section Title
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Past Charging History",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MainText
                    )
                    Text(
                        text = "$totalSessions receipts",
                        fontSize = 12.sp,
                        color = SecondaryText
                    )
                }
            }

            if (uiState.history.isEmpty()) {
                item {
                    EmptyState(
                        title = "No Past Sessions Yet",
                        message = "Scan a charger QR code or pick a hub on the map to begin your first charging session."
                    )
                }
            } else {
                items(uiState.history, key = { it.sessionId }) { session ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenSessionSummary(session.sessionId) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, Border),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 10.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Primary.copy(alpha = 0.1f),
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.EvStation,
                                                contentDescription = null,
                                                tint = Primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = session.stationName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MainText,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${session.startTime} • ${session.chargerId} (#${session.connectorId})",
                                            fontSize = 12.sp,
                                            color = SecondaryText,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SuccessLight
                                ) {
                                    Text(
                                        text = "Completed",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Success,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = AppBackground,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Energy", fontSize = 11.sp, color = SecondaryText)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("${session.totalEnergyKwh} kWh", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MainText)
                                    }
                                    Column {
                                        Text("Duration", fontSize = 11.sp, color = SecondaryText)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(session.durationFormatted, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Total Paid", fontSize = 11.sp, color = SecondaryText)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(session.finalAmount.displayAmount, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Primary)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ReceiptLong,
                                        contentDescription = null,
                                        tint = TertiaryText,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Paid via ${session.paymentMethod}",
                                        fontSize = 12.sp,
                                        color = SecondaryText
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "View Receipt",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
