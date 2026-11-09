package com.spacetime.evcharging.feature.site.sessions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.spacetime.evcharging.data.model.SiteLiveSession
import com.spacetime.evcharging.data.repository.SiteAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SiteSessionsUiState(
    val liveSessions: List<SiteLiveSession> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class SiteSessionsViewModel @Inject constructor(
    private val siteAdminRepository: SiteAdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SiteSessionsUiState())
    val uiState: StateFlow<SiteSessionsUiState> = _uiState.asStateFlow()

    init {
        loadSessions()
    }

    fun loadSessions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val res = siteAdminRepository.getLiveSessions()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, liveSessions = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }
}

@Composable
fun SiteSessionsScreen(
    viewModel: SiteSessionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Live Charging Sessions") },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Connecting to telemetry meter stream...")
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active Dispatches (${uiState.liveSessions.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                        Text("Real-time vehicle energy dispensing", fontSize = 13.sp, color = SecondaryText)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Success.copy(alpha = 0.15f)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Success))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Live Feed", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Success)
                        }
                    }
                }
            }

            items(uiState.liveSessions) { session ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Primary.copy(alpha = 0.12f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(session.chargerId, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MainText)
                                    Text("Gun #${session.connectorId}", fontSize = 12.sp, color = SecondaryText)
                                }
                            }
                            Text(session.customerMaskedMobile, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = SecondaryText)
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Border)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Delivered", fontSize = 12.sp, color = SecondaryText)
                                Text("${session.energyKwh} kWh", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MainText)
                            }
                            Column {
                                Text("Power Draw", fontSize = 12.sp, color = SecondaryText)
                                Text("${session.powerKw} kW", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                            }
                            Column {
                                Text("Estimated Value", fontSize = 12.sp, color = SecondaryText)
                                Text(session.estimatedAmount.displayAmount, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MainText)
                            }
                        }
                    }
                }
            }
        }
    }
}
