package com.spacetime.evcharging.feature.customer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.datastore.UserPreferences
import com.spacetime.evcharging.data.model.Station
import com.spacetime.evcharging.data.model.User
import com.spacetime.evcharging.data.repository.AuthRepository
import com.spacetime.evcharging.data.repository.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SupportTicketSubmission(
    val ticketNumber: String,
    val category: String,
    val description: String,
    val status: String = "OPEN"
)

data class NotificationItemData(
    val id: String,
    val title: String,
    val message: String,
    val category: String, // ALL, CHARGING, PAYMENT, OFFER
    val timestamp: String,
    val isRead: Boolean
)

data class ProfileUiState(
    val user: User? = null,
    val savedStations: List<Station> = emptyList(),
    val notifications: List<NotificationItemData> = emptyList(),
    val totalSessionsCount: Int = 14,
    val totalEnergyDispensedKwh: Double = 184.5,
    val co2SavedKg: Double = 152.0,
    val selectedLanguage: String = "English (India)",
    val isLoading: Boolean = true,
    val isLoggedOut: Boolean = false,
    val submittedTicket: SupportTicketSubmission? = null,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val stationRepository: StationRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val userRes = authRepository.getCurrentUser()
            val stationsRes = stationRepository.getNearbyStations(23.18, 79.98)

            val mockNotifications = listOf(
                NotificationItemData("notif_01", "Charging Completed", "Your session at Highway Superhub Jabalpur delivered 22.45 kWh.", "CHARGING", "10m ago", false),
                NotificationItemData("notif_02", "Wallet Auto-credited", "₹500.00 added successfully via UPI Intent.", "PAYMENT", "Yesterday", true),
                NotificationItemData("notif_03", "Green Weekend Cashback", "Get 10% instant wallet credit on all DC fast charging this Sunday.", "OFFER", "2 days ago", true)
            )

            if (userRes is AppResult.Success) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = userRes.data,
                        savedStations = (stationsRes as? AppResult.Success)?.data?.take(2) ?: emptyList(),
                        notifications = mockNotifications
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load profile") }
            }
        }
    }

    fun submitSupportTicket(category: String, description: String, sessionId: String?) {
        viewModelScope.launch {
            val tktId = "SPT-${System.currentTimeMillis().toString().takeLast(6)}"
            val ticket = SupportTicketSubmission(tktId, category, description)
            _uiState.update { it.copy(submittedTicket = ticket) }
        }
    }

    fun removeSavedStation(stationId: String) {
        _uiState.update {
            it.copy(savedStations = it.savedStations.filter { s -> s.id != stationId })
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout(logoutAll = true)
            userPreferences.clear()
            _uiState.update { it.copy(isLoggedOut = true) }
            onLoggedOut()
        }
    }

    fun clearSubmittedTicket() {
        _uiState.update { it.copy(submittedTicket = null) }
    }
}
