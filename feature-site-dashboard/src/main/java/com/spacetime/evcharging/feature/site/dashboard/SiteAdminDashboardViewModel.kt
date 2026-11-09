package com.spacetime.evcharging.feature.site.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.SiteAdminDashboard
import com.spacetime.evcharging.data.repository.SiteAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SiteAdminDashboardUiState(
    val isLoading: Boolean = true,
    val dashboard: SiteAdminDashboard? = null,
    val businessName: String = "Highway EV Hubs India",
    val error: String? = null
)

@HiltViewModel
class SiteAdminDashboardViewModel @Inject constructor(
    private val siteAdminRepository: SiteAdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SiteAdminDashboardUiState())
    val uiState: StateFlow<SiteAdminDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val res = siteAdminRepository.getDashboard()) {
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
