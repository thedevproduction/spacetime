package com.spacetime.evcharging.feature.station.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.Charger
import com.spacetime.evcharging.data.model.Connector
import com.spacetime.evcharging.data.model.Station
import com.spacetime.evcharging.data.repository.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StationDetailsUiState(
    val station: Station? = null,
    val chargers: List<Charger> = emptyList(),
    val selectedCharger: Charger? = null,
    val selectedConnector: Connector? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class StationDetailsViewModel @Inject constructor(
    private val stationRepository: StationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val stationId: String = savedStateHandle.get<String>("stationId") ?: "st_001"

    private val _uiState = MutableStateFlow(StationDetailsUiState())
    val uiState: StateFlow<StationDetailsUiState> = _uiState.asStateFlow()

    init {
        loadStationDetails(stationId)
    }

    fun loadStationDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val stationRes = stationRepository.getStationDetails(id)
            val chargersRes = stationRepository.getStationChargers(id)

            if (stationRes is AppResult.Success && chargersRes is AppResult.Success) {
                val chargers = chargersRes.data
                val defaultCharger = chargers.firstOrNull()
                val defaultConnector = defaultCharger?.connectors?.firstOrNull { it.status.isAvailableToCharge }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        station = stationRes.data,
                        chargers = chargers,
                        selectedCharger = defaultCharger,
                        selectedConnector = defaultConnector
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load station details") }
            }
        }
    }

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    fun selectConnector(charger: Charger, connector: Connector) {
        if (connector.status.isAvailableToCharge) {
            _uiState.update {
                it.copy(
                    selectedCharger = charger,
                    selectedConnector = connector
                )
            }
        }
    }
}
