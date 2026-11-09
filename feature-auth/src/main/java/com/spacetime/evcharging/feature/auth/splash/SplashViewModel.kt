package com.spacetime.evcharging.feature.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.UserRole
import com.spacetime.evcharging.core.datastore.UserPreferences
import com.spacetime.evcharging.core.navigation.*
import com.spacetime.evcharging.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        checkSession()
    }

    fun checkSession() {
        viewModelScope.launch {
            _uiState.value = SplashUiState.Loading
            delay(1200) // Brief branded splash delay

            if (!authRepository.isLoggedIn()) {
                _uiState.value = SplashUiState.SessionExpired
                _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(WelcomeRoute))
                return@launch
            }

            when (val result = authRepository.getCurrentUser()) {
                is AppResult.Success -> {
                    val user = result.data
                    val roles = user.roles
                    if (roles.size > 1) {
                        _uiState.value = SplashUiState.SessionValid(hasMultipleRoles = true)
                        _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(RoleSelectionRoute))
                    } else {
                        _uiState.value = SplashUiState.SessionValid(hasMultipleRoles = false)
                        val targetRoute: Any = when (roles.firstOrNull() ?: UserRole.CUSTOMER) {
                            UserRole.CUSTOMER -> CustomerShellRoute
                            UserRole.SITE_ADMIN -> SiteAdminShellRoute
                            UserRole.SERVICE_PROVIDER -> ProviderShellRoute
                            UserRole.SUPER_ADMIN -> CustomerShellRoute
                        }
                        _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(targetRoute))
                    }
                }
                is AppResult.Error -> {
                    _uiState.value = SplashUiState.SessionExpired
                    _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(WelcomeRoute))
                }
            }
        }
    }
}
