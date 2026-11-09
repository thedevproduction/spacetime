package com.spacetime.evcharging.feature.auth.otp

/**
 * Data class representing the UI state for the OTP Verification screen.
 */
data class OtpUiState(
    val maskedPhoneNumber: String = "",
    val otp: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val resendCountdownSeconds: Int = 30,
    val canResend: Boolean = false,
    val isOtpComplete: Boolean = false
)
