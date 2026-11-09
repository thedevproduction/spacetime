package com.spacetime.evcharging.feature.auth.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.navigation.NavigationEvent
import com.spacetime.evcharging.core.navigation.OtpVerificationRoute
import com.spacetime.evcharging.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    fun onPhoneNumberChanged(number: String) {
        val isValid = number.length == 10 && (number.startsWith("6") || number.startsWith("7") || number.startsWith("8") || number.startsWith("9"))
        _uiState.update { it.copy(phoneNumber = number, isPhoneValid = isValid, error = null) }
    }

    fun onCountryCodeChanged(code: String) {
        _uiState.update { it.copy(countryCode = code) }
    }

    fun sendOtp() {
        val state = _uiState.value
        if (!state.isPhoneValid) {
            _uiState.update { it.copy(error = "Please enter a valid 10-digit Indian mobile number") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.sendOtp(state.countryCode, state.phoneNumber)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(
                        NavigationEvent.Navigate(
                            OtpVerificationRoute(
                                challengeId = result.data.challengeId,
                                maskedNumber = result.data.maskedMobileNumber
                            )
                        )
                    )
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }
}
