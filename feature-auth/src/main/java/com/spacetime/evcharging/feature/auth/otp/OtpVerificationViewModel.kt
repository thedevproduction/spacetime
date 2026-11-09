package com.spacetime.evcharging.feature.auth.otp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.UserRole
import com.spacetime.evcharging.core.navigation.*
import com.spacetime.evcharging.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val challengeId: String = savedStateHandle.get<String>("challengeId") ?: ""
    private val maskedNumber: String = savedStateHandle.get<String>("maskedNumber") ?: ""

    private val _uiState = MutableStateFlow(
        OtpUiState(maskedPhoneNumber = maskedNumber)
    )
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    private var countdownJob: Job? = null

    init {
        startCountdown()
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            _uiState.update { it.copy(resendCountdownSeconds = 30, canResend = false) }
            for (sec in 29 downTo 0) {
                delay(1000)
                _uiState.update { it.copy(resendCountdownSeconds = sec) }
            }
            _uiState.update { it.copy(canResend = true) }
        }
    }

    fun onOtpChanged(otp: String) {
        val isComplete = otp.length == 6
        _uiState.update { it.copy(otp = otp, isOtpComplete = isComplete, error = null) }
        if (isComplete) {
            verifyOtp()
        }
    }

    fun verifyOtp() {
        val state = _uiState.value
        if (state.otp.length != 6) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.verifyOtp(challengeId, state.otp)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    val loginResult = result.data
                    if (loginResult.isNewUser) {
                        _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(OnboardingRoute))
                    } else {
                        val roles = loginResult.user.roles
                        if (roles.size > 1) {
                            _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(RoleSelectionRoute))
                        } else {
                            val route: Any = when (roles.firstOrNull() ?: UserRole.CUSTOMER) {
                                UserRole.CUSTOMER -> CustomerShellRoute
                                UserRole.SITE_ADMIN -> SiteAdminShellRoute
                                UserRole.SERVICE_PROVIDER -> ProviderShellRoute
                                UserRole.SUPER_ADMIN -> CustomerShellRoute
                            }
                            _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(route))
                        }
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun resendOtp() {
        if (!_uiState.value.canResend) return
        viewModelScope.launch {
            startCountdown()
        }
    }
}
