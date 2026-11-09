package com.spacetime.evcharging.feature.auth.splash

/**
 * Sealed interface representing the UI state for the Splash screen.
 */
sealed interface SplashUiState {

    /** Initial loading state - checking session validity. */
    data object Loading : SplashUiState

    /** Session is valid, user is authenticated. */
    data class SessionValid(
        val hasMultipleRoles: Boolean = false,
        val isNewUser: Boolean = false
    ) : SplashUiState

    /** Session has expired or user is not logged in. */
    data object SessionExpired : SplashUiState

    /** No internet connection available. */
    data object NoInternet : SplashUiState

    /** App is under maintenance. */
    data object MaintenanceMode : SplashUiState

    /** A mandatory app update is required. */
    data object UpdateRequired : SplashUiState
}
