package com.spacetime.evcharging.feature.provider.technicians

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Engineering
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
import com.spacetime.evcharging.data.model.TechnicianProfile
import com.spacetime.evcharging.data.repository.ServiceProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProviderTechniciansUiState(
    val technicians: List<TechnicianProfile> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProviderTechniciansViewModel @Inject constructor(
    private val serviceProviderRepository: ServiceProviderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProviderTechniciansUiState())
    val uiState: StateFlow<ProviderTechniciansUiState> = _uiState.asStateFlow()

    init {
        loadTechnicians()
    }

    fun loadTechnicians() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val res = serviceProviderRepository.getTechnicians()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, technicians = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}

@Composable
fun ProviderTechniciansScreen(
    viewModel: ProviderTechniciansViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Technicians & Engineers") },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Loading field engineering team...")
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
                    Text("Field Team (${uiState.technicians.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                    Text("Skilled EVSE, HT/LT electrical and civil field workforce", fontSize = 13.sp, color = SecondaryText)
                }
            }

            items(uiState.technicians) { tech ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Primary.copy(alpha = 0.12f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Default.Engineering, contentDescription = null, tint = Primary)
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(tech.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MainText)
                                Text("${tech.serviceRegion} • ${tech.mobileNumber}", fontSize = 12.sp, color = SecondaryText)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (tech.availability == "ON_DUTY") Warning.copy(alpha = 0.15f) else Success.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = if (tech.availability == "ON_DUTY") "On Duty (${tech.activeTicketsCount})" else "Available",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (tech.availability == "ON_DUTY") Warning else Success,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Skills: ${tech.skills.joinToString()}", fontSize = 12.sp, color = SecondaryText)
                    }
                }
            }
        }
    }
}
