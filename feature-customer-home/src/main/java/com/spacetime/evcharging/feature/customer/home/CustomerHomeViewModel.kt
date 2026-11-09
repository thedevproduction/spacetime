package com.spacetime.evcharging.feature.customer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.*
import com.spacetime.evcharging.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerHomeUiState(
    val userName: String = "",
    val walletBalance: String = "₹0.00",
    val isLoading: Boolean = true,
    val error: String? = null,
    val nearbyStations: List<Station> = emptyList(),
    val activeSession: ActiveChargingSession? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class CustomerHomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val walletRepository: WalletRepository,
    private val stationRepository: StationRepository,
    private val chargingRepository: ChargingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerHomeUiState())
    val uiState: StateFlow<CustomerHomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // User name
            val user = (authRepository.getCurrentUser() as? AppResult.Success)?.data
            val userName = user?.fullName ?: "Driver"

            // Wallet
            val wallet = (walletRepository.getWallet() as? AppResult.Success)?.data
            val walletBal = wallet?.availableBalance?.displayAmount ?: "₹0.00"

            // Stations near Jabalpur default coordinates
            val stations = (stationRepository.getNearbyStations(23.1815, 79.9864) as? AppResult.Success)?.data ?: emptyList()

            // Active session
            val session = (chargingRepository.getActiveSession() as? AppResult.Success)?.data

            _uiState.update {
                it.copy(
                    userName = userName,
                    walletBalance = walletBal,
                    nearbyStations = stations,
                    activeSession = session,
                    isLoading = false
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
