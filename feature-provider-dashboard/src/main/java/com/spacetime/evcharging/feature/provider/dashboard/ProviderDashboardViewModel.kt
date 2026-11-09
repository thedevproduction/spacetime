package com.spacetime.evcharging.feature.provider.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.ProviderDashboard
import com.spacetime.evcharging.data.repository.ServiceProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProviderDashboardUiState(
    val isLoading: Boolean = true,
    val dashboard: ProviderDashboard? = null,
    val providerName: String = "Spacetime Mobility Solutions",
    val error: String? = null
)

@HiltViewModel
class ProviderDashboardViewModel @Inject constructor(
    private val serviceProviderRepository: ServiceProviderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProviderDashboardUiState())
    val uiState: StateFlow<ProviderDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val res = serviceProviderRepository.getDashboard()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, dashboard = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }
}
