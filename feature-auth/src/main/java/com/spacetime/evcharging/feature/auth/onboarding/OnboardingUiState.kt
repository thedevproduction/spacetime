package com.spacetime.evcharging.feature.auth.onboarding

/**
 * Data class representing the UI state for the Onboarding / Profile Completion screen.
 */
data class OnboardingUiState(
    val fullName: String = "",
    val email: String = "",
    val city: String = "",
    val preferredLanguage: String = "en-IN",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val nameError: String? = null
)
