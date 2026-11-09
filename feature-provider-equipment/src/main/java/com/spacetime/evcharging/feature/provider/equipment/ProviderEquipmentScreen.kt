package com.spacetime.evcharging.feature.provider.equipment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.spacetime.evcharging.data.model.EquipmentItem
import com.spacetime.evcharging.data.repository.ServiceProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProviderEquipmentUiState(
    val equipment: List<EquipmentItem> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProviderEquipmentViewModel @Inject constructor(
    private val serviceProviderRepository: ServiceProviderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProviderEquipmentUiState())
    val uiState: StateFlow<ProviderEquipmentUiState> = _uiState.asStateFlow()

    init {
        loadEquipment()
    }

    fun loadEquipment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val res = serviceProviderRepository.getEquipment()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, equipment = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}

@Composable
fun ProviderEquipmentScreen(
    viewModel: ProviderEquipmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Equipment Inventory") },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Loading charger serialized asset inventory...")
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text("Serialized Hardware Assets (${uiState.equipment.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                    Text("Tracking chargers, warranties, and deployment lifecycle", fontSize = 13.sp, color = SecondaryText)
                }
            }

            items(uiState.equipment) { item ->
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
                            Text(item.serialNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MainText)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (item.status) {
                                    "INSTALLED" -> Success.copy(alpha = 0.15f)
                                    "DISPATCHED" -> Primary.copy(alpha = 0.15f)
                                    "UNDER_MAINTENANCE" -> Error.copy(alpha = 0.15f)
                                    else -> Secondary.copy(alpha = 0.08f)
                                }
                            ) {
                                Text(
                                    text = item.status.replace("_", " "),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (item.status) {
                                        "INSTALLED" -> Success
                                        "DISPATCHED" -> Primary
                                        "UNDER_MAINTENANCE" -> Error
                                        else -> Secondary
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("${item.manufacturer} • ${item.model}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MainText)
                        Text("${item.ratedPowerKw.toInt()} kW ${item.chargerType} (${item.connectorTypes.joinToString()})", fontSize = 13.sp, color = Primary, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Border)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Assigned Site", fontSize = 11.sp, color = SecondaryText)
                                Text(item.siteName, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                            }
                            Column {
                                Text("Warranty Until", fontSize = 11.sp, color = SecondaryText)
                                Text(item.warrantyExpiryDate, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                            }
                        }
                    }
                }
            }
        }
    }
}
