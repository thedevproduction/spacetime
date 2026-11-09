package com.spacetime.evcharging.feature.site.stations

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.designsystem.component.*
import com.spacetime.evcharging.core.designsystem.theme.*
import com.spacetime.evcharging.data.model.Station
import com.spacetime.evcharging.data.repository.SiteAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SiteStationsUiState(
    val stations: List<Station> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class SiteStationsViewModel @Inject constructor(
    private val siteAdminRepository: SiteAdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SiteStationsUiState())
    val uiState: StateFlow<SiteStationsUiState> = _uiState.asStateFlow()

    init {
        loadStations()
    }

    fun loadStations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val res = siteAdminRepository.getStations()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, stations = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }
}

@Composable
fun SiteStationsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SiteStationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Station Management") },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Loading your station network...")
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
                Column {
                    Text("Your EV Stations (${uiState.stations.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                    Text("Monitor station operational readiness, chargers, and tariffs.", fontSize = 13.sp, color = SecondaryText)
                }
            }

            items(uiState.stations) { station ->
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
                            Text(station.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MainText)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Success.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = if (station.isOpen) "Open Now" else "Closed",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Success,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(station.address, fontSize = 13.sp, color = SecondaryText)

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Border)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Available Connectors", fontSize = 12.sp, color = SecondaryText)
                                Text("${station.availableConnectors} / ${station.totalConnectors} Guns", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                            }
                            Column {
                                Text("Active Tariff", fontSize = 12.sp, color = SecondaryText)
                                Text("${station.minimumTariff.displayAmount} / kWh", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MainText)
                            }
                            Column {
                                Text("Operating Schedule", fontSize = 12.sp, color = SecondaryText)
                                Text(station.operatingHours, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                            }
                        }
                    }
                }
            }
        }
    }
}
