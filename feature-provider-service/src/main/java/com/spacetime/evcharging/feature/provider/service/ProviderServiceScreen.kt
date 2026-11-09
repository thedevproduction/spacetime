package com.spacetime.evcharging.feature.provider.service

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
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
import com.spacetime.evcharging.data.model.ServiceTicket
import com.spacetime.evcharging.data.repository.ServiceProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProviderServiceUiState(
    val tickets: List<ServiceTicket> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProviderServiceViewModel @Inject constructor(
    private val serviceProviderRepository: ServiceProviderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProviderServiceUiState())
    val uiState: StateFlow<ProviderServiceUiState> = _uiState.asStateFlow()

    init {
        loadTickets()
    }

    fun loadTickets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val res = serviceProviderRepository.getServiceTickets()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, tickets = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun assignTechnician(ticketId: String) {
        viewModelScope.launch {
            serviceProviderRepository.assignTicket(ticketId, "Deepak Joshi")
            loadTickets()
        }
    }

    fun resolveTicket(ticketId: String) {
        viewModelScope.launch {
            serviceProviderRepository.resolveTicket(ticketId, "Module replaced and tested with EVSE loop.")
            loadTickets()
        }
    }
}

@Composable
fun ProviderServiceScreen(
    viewModel: ProviderServiceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Service & SLA Tickets") },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Fetching open repair tickets...")
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
                    Text("Maintenance Queue (${uiState.tickets.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                    Text("Field service calls with contractual SLA deadlines", fontSize = 13.sp, color = SecondaryText)
                }
            }

            items(uiState.tickets) { ticket ->
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
                            Text(ticket.id, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Primary)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (ticket.priority == "HIGH") Error.copy(alpha = 0.15f) else Warning.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${ticket.priority} PRIORITY",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (ticket.priority == "HIGH") Error else Warning,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(ticket.problemDescription, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MainText)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${ticket.stationName} • ${ticket.chargerId}", fontSize = 13.sp, color = SecondaryText)

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Border)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("SLA Target", fontSize = 11.sp, color = SecondaryText)
                                Text(ticket.slaDueFormatted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if (ticket.priority == "HIGH") Error else MainText)
                            }
                            Column {
                                Text("Technician", fontSize = 11.sp, color = SecondaryText)
                                Text(ticket.assignedTechnician ?: "Unassigned", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Primary)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (ticket.status == "OPEN") {
                                Button(
                                    onClick = { viewModel.assignTechnician(ticket.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Secondary)
                                ) {
                                    Text("Assign Field Tech", fontSize = 13.sp)
                                }
                            } else if (ticket.status != "RESOLVED") {
                                Button(
                                    onClick = { viewModel.resolveTicket(ticket.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Success)
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mark Resolved", fontSize = 13.sp)
                                }
                            } else {
                                Text("Resolved & Closed", fontWeight = FontWeight.Bold, color = Success, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
