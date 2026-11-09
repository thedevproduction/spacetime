package com.spacetime.evcharging.feature.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.Vehicle
import com.spacetime.evcharging.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VehiclesUiState(
    val vehicles: List<Vehicle> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    // Add Vehicle form state
    val nickname: String = "",
    val manufacturer: String = "Tata Motors",
    val model: String = "Nexon EV",
    val regNumber: String = "",
    val batteryCapacityKwh: String = "40.5",
    val selectedConnectors: Set<String> = setOf("CCS2"),
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehiclesUiState())
    val uiState: StateFlow<VehiclesUiState> = _uiState.asStateFlow()

    init {
        loadVehicles()
    }

    fun loadVehicles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val res = vehicleRepository.getVehicles()) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, vehicles = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }

    fun onNicknameChanged(value: String) = _uiState.update { it.copy(nickname = value) }
    fun onManufacturerChanged(value: String) = _uiState.update { it.copy(manufacturer = value) }
    fun onModelChanged(value: String) = _uiState.update { it.copy(model = value) }
    fun onRegNumberChanged(value: String) = _uiState.update { it.copy(regNumber = value) }
    fun onBatteryCapacityChanged(value: String) = _uiState.update { it.copy(batteryCapacityKwh = value) }

    fun toggleConnector(connector: String) {
        _uiState.update {
            val current = it.selectedConnectors.toMutableSet()
            if (current.contains(connector)) {
                if (current.size > 1) current.remove(connector)
            } else {
                current.add(connector)
            }
            it.copy(selectedConnectors = current)
        }
    }

    fun saveVehicle() {
        val state = _uiState.value
        val cap = state.batteryCapacityKwh.toDoubleOrNull() ?: 40.0
        val vehicle = Vehicle(
            id = "",
            nickname = state.nickname.ifBlank { "${state.manufacturer} ${state.model}" },
            category = "FOUR_WHEELER",
            manufacturer = state.manufacturer,
            model = state.model,
            registrationNumber = state.regNumber.ifBlank { null },
            batteryCapacityKwh = cap,
            connectorTypes = state.selectedConnectors.toList(),
            isDefault = state.vehicles.isEmpty()
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            when (val res = vehicleRepository.addVehicle(vehicle)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                    loadVehicles()
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isSaving = false, error = res.message) }
                }
            }
        }
    }

    fun setDefaultVehicle(vehicleId: String) {
        viewModelScope.launch {
            vehicleRepository.setDefaultVehicle(vehicleId)
            loadVehicles()
        }
    }

    fun deleteVehicle(vehicleId: String) {
        viewModelScope.launch {
            vehicleRepository.deleteVehicle(vehicleId)
            loadVehicles()
        }
    }

    fun resetSaveState() {
        _uiState.update {
            it.copy(
                nickname = "",
                regNumber = "",
                isSaving = false,
                saveSuccess = false
            )
        }
    }
}
