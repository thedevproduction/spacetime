package com.spacetime.evcharging.feature.auth.welcome

/**
 * Data class representing the UI state for the Welcome / Login screen.
 */
data class WelcomeUiState(
    val countryCode: String = "+91",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPhoneValid: Boolean = false
)
