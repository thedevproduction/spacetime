package com.spacetime.evcharging.feature.auth.roleselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.core.common.model.UserRole
import com.spacetime.evcharging.core.datastore.UserPreferences
import com.spacetime.evcharging.core.navigation.*
import com.spacetime.evcharging.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoleSelectionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoleSelectionUiState())
    val uiState: StateFlow<RoleSelectionUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    init {
        loadUserRoles()
    }

    private fun loadUserRoles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = authRepository.getCurrentUser()) {
                is AppResult.Success -> {
                    val user = result.data
                    val options = createRoleOptions(user.roles)
                    _uiState.update {
                        it.copy(
                            userName = user.fullName,
                            availableRoles = options,
                            isLoading = false
                        )
                    }
                }
                is AppResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun selectRole(role: UserRole) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.switchRole(role)
            userPreferences.setActiveRole(role.roleKey)
            _uiState.update { it.copy(isLoading = false) }

            val targetRoute: Any = when (role) {
                UserRole.CUSTOMER -> CustomerShellRoute
                UserRole.SITE_ADMIN -> SiteAdminShellRoute
                UserRole.SERVICE_PROVIDER -> ProviderShellRoute
                UserRole.SUPER_ADMIN -> CustomerShellRoute
            }
            _navigationEvent.emit(NavigationEvent.NavigateAndClearStack(targetRoute))
        }
    }
}
