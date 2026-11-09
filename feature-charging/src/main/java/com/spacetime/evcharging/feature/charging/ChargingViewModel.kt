package com.spacetime.evcharging.feature.charging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.*
import com.spacetime.evcharging.data.model.*
import com.spacetime.evcharging.data.repository.ChargingRepository
import com.spacetime.evcharging.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChargingUiState(
    val quote: ChargingQuote? = null,
    val selectedPaymentMethod: String = "WALLET", // WALLET, UPI, CARD
    val activeSession: ActiveChargingSession? = null,
    val sessionSummary: SessionSummary? = null,
    val sessionState: SessionStatus = SessionStatus.CREATED,
    val isLoading: Boolean = false,
    val isStopping: Boolean = false,
    val showStopConfirmSheet: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChargingViewModel @Inject constructor(
    private val chargingRepository: ChargingRepository,
    private val walletRepository: WalletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val quoteIdArg: String? = savedStateHandle.get<String>("quoteId")
    private val sessionIdArg: String? = savedStateHandle.get<String>("sessionId")

    private val _uiState = MutableStateFlow(ChargingUiState())
    val uiState: StateFlow<ChargingUiState> = _uiState.asStateFlow()

    private var telemetryJob: Job? = null

    init {
        quoteIdArg?.let { loadQuote(it) }
        sessionIdArg?.let { resumeOrLoadSession(it) }
    }

    fun loadQuote(quoteId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val res = chargingRepository.getQuote(quoteId)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, quote = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }

    fun selectPaymentMethod(method: String) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun startCharging(onStarted: (String) -> Unit) {
        val q = _uiState.value.quote ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, sessionState = SessionStatus.STARTING) }

            // Controlled state machine: check eligibility -> send start -> wait for charger ack
            when (val res = chargingRepository.startSession(q.quoteId, _uiState.value.selectedPaymentMethod)) {
                is AppResult.Success -> {
                    val sId = res.data
                    _uiState.update { it.copy(isLoading = false, sessionState = SessionStatus.CHARGING) }
                    startTelemetryLoop(sId)
                    onStarted(sId)
                }
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sessionState = SessionStatus.FAILED,
                            error = res.message
                        )
                    }
                }
            }
        }
    }

    fun resumeOrLoadSession(sessionId: String) {
        viewModelScope.launch {
            startTelemetryLoop(sessionId)
        }
    }

    private fun startTelemetryLoop(sessionId: String) {
        telemetryJob?.cancel()
        telemetryJob = viewModelScope.launch {
            while (true) {
                when (val res = chargingRepository.getActiveSession()) {
                    is AppResult.Success -> {
                        val session = res.data
                        if (session != null) {
                            _uiState.update {
                                it.copy(
                                    activeSession = session,
                                    sessionState = session.status
                                )
                            }
                        }
                    }
                    is AppResult.Error -> {}
                }
                delay(3000) // Poll telemetry every 3 seconds
            }
        }
    }

    fun showStopConfirmation(show: Boolean) {
        _uiState.update { it.copy(showStopConfirmSheet = show) }
    }

    fun stopCharging(onStopped: (String) -> Unit) {
        val session = _uiState.value.activeSession ?: return
        viewModelScope.launch {
            telemetryJob?.cancel()
            _uiState.update {
                it.copy(
                    showStopConfirmSheet = false,
                    isStopping = true,
                    sessionState = SessionStatus.STOPPING
                )
            }

            when (val res = chargingRepository.stopSession(session.sessionId)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isStopping = false,
                            activeSession = null,
                            sessionSummary = res.data,
                            sessionState = SessionStatus.COMPLETED
                        )
                    }
                    onStopped(session.sessionId)
                }
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isStopping = false,
                            error = "Failed to stop session: ${res.message}"
                        )
                    }
                }
            }
        }
    }

    fun loadSummary(sessionId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val res = chargingRepository.getSessionSummary(sessionId)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, sessionSummary = res.data) }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }
}
