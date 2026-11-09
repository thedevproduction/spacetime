package com.spacetime.evcharging.feature.provider.installations

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Construction
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
import com.spacetime.evcharging.data.model.InstallationProject
import com.spacetime.evcharging.data.repository.ServiceProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProviderInstallationsUiState(
    val installations: List<InstallationProject> = emptyList(),
    val isLoading: Boolean = true,
    val showCreateDialog: Boolean = false,
    val newCustomer: String = "",
    val newSite: String = "",
    val newAddress: String = "",
    val newCount: String = "2"
)

@HiltViewModel
class ProviderInstallationsViewModel @Inject constructor(
    private val serviceProviderRepository: ServiceProviderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProviderInstallationsUiState())
    val uiState: StateFlow<ProviderInstallationsUiState> = _uiState.asStateFlow()

    init {
        loadInstallations()
    }

    fun loadInstallations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val res = serviceProviderRepository.getInstallations()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, installations = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun showCreateDialog(show: Boolean) = _uiState.update { it.copy(showCreateDialog = show) }
    fun onCustomerChanged(v: String) = _uiState.update { it.copy(newCustomer = v) }
    fun onSiteChanged(v: String) = _uiState.update { it.copy(newSite = v) }
    fun onAddressChanged(v: String) = _uiState.update { it.copy(newAddress = v) }
    fun onCountChanged(v: String) = _uiState.update { it.copy(newCount = v) }

    fun createInstallation() {
        val s = _uiState.value
        if (s.newCustomer.isBlank() || s.newSite.isBlank()) return

        val project = InstallationProject(
            id = "",
            customerName = s.newCustomer,
            siteName = s.newSite,
            address = s.newAddress.ifBlank { "Madhya Pradesh, India" },
            chargerCount = s.newCount.toIntOrNull() ?: 2,
            scheduledDate = "30 Nov 2026",
            status = "SURVEY_PENDING",
            technicianName = null,
            electricalSurveyDone = false,
            commissioningDone = false
        )

        viewModelScope.launch {
            serviceProviderRepository.createInstallation(project)
            _uiState.update { it.copy(showCreateDialog = false, newCustomer = "", newSite = "", newAddress = "") }
            loadInstallations()
        }
    }
}

@Composable
fun ProviderInstallationsScreen(
    viewModel: ProviderInstallationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Installation Pipeline") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showCreateDialog(true) },
                containerColor = Primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New Installation")
            }
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Loading installation jobs & surveys...")
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
                    Text("Commissioning & Turnkey Pipeline (${uiState.installations.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                    Text("Survey, mounting, electrical panel & grid sign-off tracking", fontSize = 13.sp, color = SecondaryText)
                }
            }

            items(uiState.installations) { inst ->
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
                            Text(inst.id, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Primary)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (inst.status) {
                                    "COMMISSIONING_PENDING" -> Success.copy(alpha = 0.15f)
                                    "INSTALLATION_IN_PROGRESS" -> Color(0xFF2563EB).copy(alpha = 0.15f)
                                    else -> Warning.copy(alpha = 0.15f)
                                }
                            ) {
                                Text(
                                    text = inst.status.replace("_", " "),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (inst.status) {
                                        "COMMISSIONING_PENDING" -> Success
                                        "INSTALLATION_IN_PROGRESS" -> Color(0xFF2563EB)
                                        else -> Warning
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(inst.siteName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MainText)
                        Text(inst.customerName, fontSize = 13.sp, color = SecondaryText)
                        Text(inst.address, fontSize = 12.sp, color = SecondaryText)

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Border)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Equipment Scope", fontSize = 11.sp, color = SecondaryText)
                                Text("${inst.chargerCount} DC Chargers", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MainText)
                            }
                            Column {
                                Text("Scheduled Date", fontSize = 11.sp, color = SecondaryText)
                                Text(inst.scheduledDate, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MainText)
                            }
                            Column {
                                Text("Lead Technician", fontSize = 11.sp, color = SecondaryText)
                                Text(inst.technicianName ?: "Pending Assignment", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = if (inst.technicianName != null) Primary else Warning)
                            }
                        }
                    }
                }
            }
        }

        if (uiState.showCreateDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showCreateDialog(false) },
                title = { Text("New Turnkey Installation", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedInput(value = uiState.newCustomer, onValueChange = viewModel::onCustomerChanged, label = "Customer / Client Name")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedInput(value = uiState.newSite, onValueChange = viewModel::onSiteChanged, label = "Site Name")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedInput(value = uiState.newAddress, onValueChange = viewModel::onAddressChanged, label = "Address & GPS")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedInput(value = uiState.newCount, onValueChange = viewModel::onCountChanged, label = "Charger Quantity")
                    }
                },
                confirmButton = {
                    Button(onClick = viewModel::createInstallation, colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                        Text("Create Job")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showCreateDialog(false) }) {
                        Text("Cancel", color = SecondaryText)
                    }
                },
                containerColor = CardBackground
            )
        }
    }
}
