package com.spacetime.evcharging.feature.auth.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.datastore.UserPreferences
import com.spacetime.evcharging.core.navigation.CustomerShellRoute
import com.spacetime.evcharging.core.navigation.NavigationEvent
import com.spacetime.evcharging.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(fullName = name, nameError = null) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onCityChanged(city: String) {
        _uiState.update { it.copy(city = city) }
    }

    fun onTermsAcceptedChanged(accepted: Boolean) {
        _uiState.update { it.copy(termsAccepted = accepted) }
    }

    fun createAccount() {
        val state = _uiState.value
        if (state.fullName.isBlank()) {
            _uiState.update { it.copy(nameError = "Full name is required") }
            return
        }
        if (!state.termsAccepted) {
            _uiState.update { it.copy(error = "Please accept the Terms and Conditions") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = authRepository.updateProfile(state.fullName, state.email, state.city, state.preferredLanguage)) {
                is AppResult.Success -> {
                    userPreferences.setOnboardingCompleted(true)
                    _uiState.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(CustomerShellRoute))
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }
}
