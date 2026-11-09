package com.spacetime.evcharging.feature.qr.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.ChargingQuote
import com.spacetime.evcharging.data.repository.ChargingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QrScannerUiState(
    val isTorchOn: Boolean = false,
    val isResolving: Boolean = false,
    val resolvedQuote: ChargingQuote? = null,
    val error: String? = null,
    val isManualInputDialogVisible: Boolean = false,
    val manualChargerId: String = ""
)

@HiltViewModel
class QrScannerViewModel @Inject constructor(
    private val chargingRepository: ChargingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QrScannerUiState())
    val uiState: StateFlow<QrScannerUiState> = _uiState.asStateFlow()

    fun toggleTorch() {
        _uiState.update { it.copy(isTorchOn = !it.isTorchOn) }
    }

    fun showManualInputDialog(show: Boolean) {
        _uiState.update { it.copy(isManualInputDialogVisible = show, error = null) }
    }

    fun onManualChargerIdChanged(id: String) {
        _uiState.update { it.copy(manualChargerId = id) }
    }

    fun resolveQr(payload: String) {
        if (_uiState.value.isResolving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isResolving = true, error = null) }
            when (val res = chargingRepository.resolveQr(payload)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isResolving = false, resolvedQuote = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isResolving = false,
                            error = "Unable to identify this charger. Please scan the QR code attached to the charger."
                        )
                    }
                }
            }
        }
    }

    fun resolveManualId() {
        val id = _uiState.value.manualChargerId
        if (id.isBlank()) return
        showManualInputDialog(false)
        resolveQr("EVCHARGE:$id")
    }

    fun clearResolvedQuote() {
        _uiState.update { it.copy(resolvedQuote = null) }
    }
}
