package com.spacetime.evcharging.feature.site.employees

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
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
import com.spacetime.evcharging.data.model.SiteEmployee
import com.spacetime.evcharging.data.repository.SiteAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SiteEmployeesUiState(
    val employees: List<SiteEmployee> = emptyList(),
    val isLoading: Boolean = true,
    val showInviteDialog: Boolean = false,
    val inviteName: String = "",
    val inviteMobile: String = "",
    val inviteRole: String = "Station Operator"
)

@HiltViewModel
class SiteEmployeesViewModel @Inject constructor(
    private val siteAdminRepository: SiteAdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SiteEmployeesUiState())
    val uiState: StateFlow<SiteEmployeesUiState> = _uiState.asStateFlow()

    init {
        loadEmployees()
    }

    fun loadEmployees() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val res = siteAdminRepository.getEmployees()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, employees = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun showInviteDialog(show: Boolean) = _uiState.update { it.copy(showInviteDialog = show) }
    fun onNameChanged(v: String) = _uiState.update { it.copy(inviteName = v) }
    fun onMobileChanged(v: String) = _uiState.update { it.copy(inviteMobile = v) }
    fun onRoleChanged(v: String) = _uiState.update { it.copy(inviteRole = v) }

    fun inviteEmployee() {
        val s = _uiState.value
        if (s.inviteName.isBlank() || s.inviteMobile.isBlank()) return

        viewModelScope.launch {
            siteAdminRepository.inviteEmployee(s.inviteName, s.inviteMobile, s.inviteRole, "Highway EV Superhub Jabalpur")
            _uiState.update { it.copy(showInviteDialog = false, inviteName = "", inviteMobile = "") }
            loadEmployees()
        }
    }
}

@Composable
fun SiteEmployeesScreen(
    viewModel: SiteEmployeesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { SpacetimeTopBar(title = "Employees & Staff") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showInviteDialog(true) },
                containerColor = Primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Invite")
            }
        },
        containerColor = AppBackground
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen(message = "Loading staff directory...")
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
                    Text("Authorized Personnel (${uiState.employees.size})", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MainText)
                    Text("Grant role-based access for managers, operators, and accountants.", fontSize = 13.sp, color = SecondaryText)
                }
            }

            items(uiState.employees) { emp ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, Border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Primary.copy(alpha = 0.12f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Primary)
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(emp.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MainText)
                            Text(emp.mobileNumber, fontSize = 12.sp, color = SecondaryText)
                            Text(emp.assignedSite, fontSize = 12.sp, color = Primary, fontWeight = FontWeight.Medium)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Secondary.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text = emp.role,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Secondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        if (uiState.showInviteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showInviteDialog(false) },
                title = { Text("Invite Staff Member", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        OutlinedInput(value = uiState.inviteName, onValueChange = viewModel::onNameChanged, label = "Full Name")
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedInput(value = uiState.inviteMobile, onValueChange = viewModel::onMobileChanged, label = "Mobile Number")
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Role: ${uiState.inviteRole}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Primary)
                    }
                },
                confirmButton = {
                    Button(onClick = viewModel::inviteEmployee, colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                        Text("Send Invite")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showInviteDialog(false) }) {
                        Text("Cancel", color = SecondaryText)
                    }
                },
                containerColor = CardBackground
            )
        }
    }
}
